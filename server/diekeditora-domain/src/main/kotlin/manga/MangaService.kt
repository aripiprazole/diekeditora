package com.diekeditora.domain.manga

import com.diekeditora.domain.page.AppPage

interface MangaService {
    suspend fun findMangas(
        first: Int,
        after: String,
        orderBy: MangaSort = MangaSort.empty(),
        filterBy: Set<String> = emptySet(),
    ): AppPage<Manga>

    suspend fun findMangaByTitle(title: String): Manga?

    suspend fun createManga(manga: Manga): Manga

    suspend fun updateManga(manga: Manga): Manga

    suspend fun deleteManga(manga: Manga): Manga
}
