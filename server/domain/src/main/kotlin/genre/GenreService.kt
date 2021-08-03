package com.diekeditora.domain.genre

interface GenreService {
    suspend fun findGenres(): List<Genre>

    suspend fun createGenre(title: String): Genre

    suspend fun deleteGenre(title: String)
}
