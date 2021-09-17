package com.diekeditora.genre.infra

import com.diekeditora.genre.domain.Genre
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface GenreRepo : CoroutineSortingRepository<Genre, UUID> {
    suspend fun findByName(name: String): Genre?
}
