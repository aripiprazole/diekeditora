package com.diekeditora.domain.genre

import com.diekeditora.domain.validation.NotBlank
import com.diekeditora.domain.validation.Size

@Suppress("Detekt.MagicNumber")
data class GenreInput(@NotBlank @Size(min = 4, max = 32) val name: String) {
    fun toGenre(): Genre {
        return Genre(name = name)
    }

    companion object {
        fun from(genre: Genre): GenreInput {
            return GenreInput(name = genre.name)
        }
    }
}
