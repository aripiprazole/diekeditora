package com.diekeditora.domain.manga

import com.diekeditora.domain.profile.Profile
import graphql.relay.Connection

interface MangaService {
    suspend fun findMangas(
        first: Int,
        after: String,
        orderBy: MangaSort = MangaSort.empty(),
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findMangasByProfile(profile: Profile): List<Manga>

    suspend fun findMangaByTitle(title: String): Manga?

    suspend fun saveManga(manga: Manga): Manga

    suspend fun updateManga(manga: Manga): Manga

    suspend fun deleteManga(manga: Manga): Manga
}
