package com.diekeditora.domain.genre

import com.diekeditora.domain.graphql.Max
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank

@Suppress("Detekt.MagicNumber")
data class GenreInput(@NotBlank @Max(32) @Min(4) val name: String) {
    fun toGenre(): Genre {
        return Genre(name = name)
    }

    companion object {
        fun from(genre: Genre): GenreInput {
            return GenreInput(name = genre.name)
        }
    }
}
