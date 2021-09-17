package com.diekeditora.chapter.domain

import com.diekeditora.MutableEntity
import com.diekeditora.BelongsTo
import com.diekeditora.file.domain.ChapterCoverKind
import com.diekeditora.file.domain.FileKind
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.page.domain.OrderBy
import com.diekeditora.shared.refs.ChapterId
import com.diekeditora.shared.refs.MangaId
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("chapter")
data class Chapter(
    @GraphQLIgnore
    @Id override val id: ChapterId = ChapterId.New,
    @Cursor val uid: UniqueId,
    val title: String,
    val index: Int,
    val enabled: Boolean = false,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val releasedOn: LocalDate? = null,
    val deletedAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    @GraphQLIgnore override val ownerId: MangaId,
) : MutableEntity<Chapter, ChapterId>, BelongsTo<MangaId> {
    @GraphQLDescription("Returns the chapter's cover")
    suspend fun cover(env: DataFetchingEnvironment): String {
        return env
            .getDataLoader<FileKind, String>("FileLinkLoader")
            .load(ChapterCoverKind(this))
            .await()
    }

    @GraphQLDescription("Returns the chapter's pages")
    suspend fun pages(env: DataFetchingEnvironment): List<String> {
        return env
            .getDataLoader<Chapter, List<String>>("ChapterPagesLoader")
            .load(this)
            .await()
    }

    @GraphQLIgnore
    override fun update(with: Chapter): Chapter {
        return copy(
            title = with.title,
            enabled = with.enabled,
            releasedOn = with.releasedOn,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chapter

        if (title != other.title) return false
        if (enabled != other.enabled) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + enabled.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Chapter(" +
            "title='$title', " +
            "enabled=$enabled, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
