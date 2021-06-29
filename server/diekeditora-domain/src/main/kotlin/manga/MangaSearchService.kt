package com.diekeditora.domain.manga

import com.diekeditora.domain.page.Page

interface MangaSearchService {
    suspend fun searchMangas(genres: Set<String>, sort: MangaSort = MangaSort.empty()): Page<Manga>
}
