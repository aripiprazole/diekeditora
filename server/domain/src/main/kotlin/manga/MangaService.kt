package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import graphql.relay.Connection

interface MangaService {
    suspend fun findMangas(
        first: Int,
        after: UniqueId? = null,
        orderBy: MangaSort = MangaSort.empty(),
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findAuthorsByManga(manga: Manga): List<Profile>

    suspend fun findMangaByUid(uid: UniqueId): Manga?

    suspend fun saveManga(input: MangaInput): Manga

    suspend fun updateManga(manga: Manga, input: MangaInput): Manga

    suspend fun deleteManga(manga: Manga): Manga
}
