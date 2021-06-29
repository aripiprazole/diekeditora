package com.diekeditora.domain.manga

import com.diekeditora.domain.page.Page

interface MangaService {
    suspend fun findMangas(page: Int = 1, sort: MangaSort = MangaSort.empty()): Page<Manga>

    suspend fun findMangaByTitle(title: String): Manga?

    suspend fun createManga(manga: Manga): Manga

    suspend fun updateManga(manga: Manga): Manga

    suspend fun deleteManga(manga: Manga)
}
