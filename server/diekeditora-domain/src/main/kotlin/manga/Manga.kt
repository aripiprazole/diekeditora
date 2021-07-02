package com.diekeditora.domain.manga

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("manga")
data class Manga(
    @Id
    @GraphQLIgnore
    @JsonIgnore
    val id: UniqueId? = null,
    val title: String,
    val competing: Boolean,
    val summary: String,
    val advisory: AgeAdvisory = AgeAdvisory.Free,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) : MutableEntity<Manga> {

    @GraphQLDescription("Returns manga's summary rating")
    suspend fun summaryRating(env: DataFetchingEnvironment): Rating {
        return env
            .getDataLoader<Manga, Rating>("MangaRatingLoader")
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
    suspend fun chapters(env: DataFetchingEnvironment): List<Chapter> {
        return env
            .getDataLoader<Manga, Set<Chapter>>("MangaChapterLoader")
            .load(this)
            .await()
            .toList()
    }

    @GraphQLDescription("Returns manga's authors")
    suspend fun authors(env: DataFetchingEnvironment): List<Profile> {
        return env
            .getDataLoader<Manga, Set<Profile>>("MangaAuthorLoader")
            .load(this)
            .await()
            .toList()
    }

    override fun update(with: Manga): Manga {
        return copy(
            title = with.title,
            competing = with.competing,
            summary = with.summary,
            advisory = with.advisory,
            updatedAt = LocalDateTime.now(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Manga

        if (title != other.title) return false
        if (competing != other.competing) return false
        if (summary != other.summary) return false
        if (advisory != other.advisory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + competing.hashCode()
        result = 31 * result + summary.hashCode()
        result = 31 * result + advisory.hashCode()
        return result
    }

    override fun toString(): String {
        return "Manga(" +
            "title='$title', " +
            "competing=$competing, " +
            "summary='$summary', " +
            "advisory=$advisory, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            "deletedAt=$deletedAt" +
            ")"
    }
}
