package com.diekeditora.infra.genre

import com.diekeditora.domain.genre.Genre
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface GenreRepo : CoroutineSortingRepository<Genre, UUID> {
    suspend fun findByName(name: String): Genre?
}
