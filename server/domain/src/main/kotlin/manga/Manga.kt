package com.diekeditora.domain.manga

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.chapter.Chapter
import com.diekeditora.domain.dataloader.PaginationArg
import com.diekeditora.domain.dataloader.toPaginationArg
import com.diekeditora.domain.genre.Genre
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.diekeditora.domain.profile.Profile
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("manga")
data class Manga(
    @GraphQLIgnore
    @Id override val id: UniqueId? = null,
    @Cursor val uid: UniqueId,
    val title: String,
    val competing: Boolean,
    val description: String,
    val advisory: Int? = null,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) : MutableEntity<Manga> {
    companion object Permissions {
        const val STORE = "manga.store"
        const val UPDATE = "manga.update"
        const val DESTROY = "manga.destroy"
    }

    @GraphQLDescription("Returns latest chapter")
    suspend fun latestChapter(env: DataFetchingEnvironment): Chapter {
        return env
            .getDataLoader<Manga, Chapter>("MangaLatestChapterLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns manga's ratings")
    suspend fun ratings(env: DataFetchingEnvironment): List<Int> {
        return env
            .getDataLoader<Manga, Set<Int>>("MangaRatingsLoader")
            .load(this)
            .await()
            .toList()
    }

    @GraphQLDescription("Returns manga's genres")
    suspend fun genres(env: DataFetchingEnvironment): List<Genre> {
        return env
            .getDataLoader<Manga, Set<Genre>>("MangaGenreLoader")
            .load(this)
            .await()
            .toList()
    }

    @GraphQLDescription("Returns manga's chapters")
    suspend fun chapters(
        env: DataFetchingEnvironment,
        first: Int,
        after: String? = null
    ): Connection<Chapter> {
        return env
            .getDataLoader<PaginationArg<Manga, String>, Connection<Chapter>>("MangaChapterLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    @GraphQLDescription("Returns manga's authors")
    suspend fun authors(env: DataFetchingEnvironment): List<Profile> {
        return env
            .getDataLoader<Manga, Set<Profile>>("MangaAuthorLoader")
            .load(this)
            .await()
            .toList()
    }

    @GraphQLIgnore
    override fun update(with: Manga): Manga {
        return copy(
            title = with.title,
            competing = with.competing,
            description = with.description,
            advisory = with.advisory,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Manga

        if (title != other.title) return false
        if (competing != other.competing) return false
        if (description != other.description) return false
        if (advisory != other.advisory) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + competing.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + advisory.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Manga(" +
            "title='$title', " +
            "competing=$competing, " +
            "summary='$description', " +
            "advisory=$advisory, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            "deletedAt=$deletedAt" +
            ")"
    }
}
