package com.diekeditora.domain.manga

enum class MangaSort {
    Empty, Recent, Older, MostRead, BestRated;

    companion object {
        fun empty(): MangaSort = Empty
    }
}
