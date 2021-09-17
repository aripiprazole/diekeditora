package com.diekeditora.genre.infra

import com.diekeditora.genre.domain.Genre
import com.diekeditora.genre.domain.GenreService
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class GenreServiceImpl(val genreRepo: GenreRepo) : GenreService {
    override suspend fun findGenres(): List<Genre> {
        return genreRepo.findAll().toList()
    }

    override suspend fun createGenre(title: String): Genre {
        return genreRepo.save(Genre(name = title))
    }

    override suspend fun deleteGenre(title: String) {
        val genre = genreRepo.findByName(title) ?: return

        genreRepo.delete(genre)
    }
}
