package com.diekeditora.domain.genre

import com.diekeditora.domain.graphql.Max
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank

@Suppress("Detekt.MagicNumber")
data class GenreInput(@NotBlank @Max(32) @Min(4) val title: String) {
    fun toGenre(): Genre {
        return Genre(title = title)
    }

    companion object {
        fun from(genre: Genre): GenreInput {
            return GenreInput(title = genre.title)
        }
    }
}
