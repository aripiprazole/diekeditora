package com.diekeditora.domain.genre

@Suppress("Detekt.MagicNumber")
data class GenreInput(val name: String) {
    fun toGenre(): Genre {
        return Genre(name = name)
    }

    companion object {
        fun from(genre: Genre): GenreInput {
            return GenreInput(name = genre.name)
        }
    }
}
