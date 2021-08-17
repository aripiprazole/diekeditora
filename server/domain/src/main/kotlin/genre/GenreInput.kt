package com.diekeditora.domain.genre

import org.valiktor.functions.hasSize
import org.valiktor.validate

@Suppress("Detekt.MagicNumber")
data class GenreInput(val name: String) {
    fun toGenre(): Genre = validate(Genre(name = name)) {
        validate(Genre::name).hasSize(min = 4, max = 32)
    }

    companion object {
        fun from(genre: Genre): GenreInput {
            return GenreInput(name = genre.name)
        }
    }
}
