package com.diekeditora.domain.manga

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.profile.Profile

interface MangaService {
    suspend fun findMangas(
        first: Int,
        after: String,
        orderBy: MangaSort = MangaSort.empty(),
        filterBy: Set<String> = emptySet(),
    ): AppPage<Manga>

    suspend fun findMangaByTitle(title: String): Manga?

    suspend fun saveManga(manga: Manga): Manga

    suspend fun updateManga(manga: Manga): Manga

    suspend fun deleteManga(manga: Manga): Manga

    suspend fun findMangasByProfile(
        profile: Profile,
        first: Int,
        after: String? = null,
        orderBy: MangaSort = MangaSort.empty(),
        filterBy: Set<String> = emptySet(),
    ): List<Manga>
}
