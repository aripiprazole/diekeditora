package com.diekeditora.manga.domain

import com.diekeditora.MutableEntity
import com.diekeditora.chapter.domain.Chapter
import com.diekeditora.genre.domain.Genre
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.page.domain.OrderBy
import com.diekeditora.profile.domain.Profile
import com.diekeditora.shared.infra.PaginationArg
import com.diekeditora.shared.infra.toPaginationArg
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
    @GraphQLDescription("Returns the manga's latest chapter")
    suspend fun latestChapter(env: DataFetchingEnvironment): Chapter {
        return env
            .getDataLoader<Manga, Chapter>("MangaLatestChapterLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns the manga's total ratings")
    suspend fun totalRatings(env: DataFetchingEnvironment): Int {
        return env
            .getDataLoader<Manga, Int>("MangaRatingsLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns the manga's ratings")
    suspend fun ratings(env: DataFetchingEnvironment): List<Int> {
        return env
            .getDataLoader<Manga, Set<Int>>("MangaRatingsLoader")
            .load(this)
            .await()
            .toList()
    }

    @GraphQLDescription("Returns the manga's genres")
    suspend fun genres(env: DataFetchingEnvironment): List<Genre> {
        return env
            .getDataLoader<Manga, Set<Genre>>("MangaGenreLoader")
            .load(this)
            .await()
            .toList()
    }

    @GraphQLDescription("Returns the manga's chapters")
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
