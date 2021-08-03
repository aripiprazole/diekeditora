package com.diekeditora.infra.genre

import com.diekeditora.domain.genre.Genre
import com.diekeditora.domain.genre.GenreService
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
internal class GenreServiceImpl(val genreRepo: GenreRepo) : GenreService {
    override suspend fun findGenres(): List<Genre> {
        return genreRepo.findAll().toList()
    }

    override suspend fun createGenre(title: String): Genre {
        return genreRepo.save(Genre(title = title))
    }

    override suspend fun deleteGenre(title: String) {
        val genre = genreRepo.findByTitle(title) ?: return

        genreRepo.delete(genre)
    }
}
