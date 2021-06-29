package com.diekeditora.domain.manga

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
) {
    @GraphQLDescription("Returns manga's summary rating")
    suspend fun summaryRating(env: DataFetchingEnvironment): Rating {
        return env
            .getDataLoader<Manga, Rating>("MangaRatingLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns manga's ratings")
    suspend fun ratings(env: DataFetchingEnvironment): Set<Int> {
        return env
            .getDataLoader<Manga, Set<Int>>("MangaRatingsLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns manga's genres")
    suspend fun genres(env: DataFetchingEnvironment): Set<Genre> {
        return env
            .getDataLoader<Manga, Set<Genre>>("MangaGenreLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns manga's chapters")
    suspend fun chapters(env: DataFetchingEnvironment): Set<Chapter> {
        return env
            .getDataLoader<Manga, Set<Chapter>>("MangaChapterLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns manga's authors")
    suspend fun authors(env: DataFetchingEnvironment): Set<Profile> {
        return env
            .getDataLoader<Manga, Set<Profile>>("MangaAuthorLoader")
            .load(this)
            .await()
    }
}
