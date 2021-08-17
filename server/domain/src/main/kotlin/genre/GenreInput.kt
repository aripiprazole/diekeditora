package com.diekeditora.domain.genre

import org.valiktor.functions.hasSize
import org.valiktor.validate

@Suppress("Detekt.MagicNumber")
data class GenreInput(val name: String) {
    init {
        validate(this) {
            validate(GenreInput::name).hasSize(min = 4, max = 32)
        }
    }

    fun toGenre(): Genre {
        return Genre(name = name)
    }

    companion object {
        fun from(genre: Genre): GenreInput {
            return GenreInput(name = genre.name)
        }
    }
}
