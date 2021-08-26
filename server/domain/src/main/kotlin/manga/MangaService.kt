package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.user.User
import graphql.relay.Connection

interface MangaService {
    suspend fun findMangas(
        first: Int,
        after: UniqueId? = null,
        orderBy: MangaSort = MangaSort.Empty,
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findSimilarMangas(
        manga: Manga,
        first: Int,
        after: UniqueId? = null,
        orderBy: MangaSort = MangaSort.Empty,
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findRecommendedMangas(
        user: User,
        first: Int,
        after: UniqueId? = null,
        orderBy: MangaSort = MangaSort.Empty,
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findFavoriteMangas(
        user: User,
        first: Int,
        after: UniqueId? = null,
        orderBy: MangaSort = MangaSort.Empty,
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findLastReadMangas(
        user: User,
        first: Int,
        after: UniqueId? = null,
        orderBy: MangaSort = MangaSort.Empty,
        filterBy: Set<String> = emptySet(),
    ): Connection<Manga>

    suspend fun findAuthorsByManga(manga: Manga): List<Profile>

    suspend fun findMangaByUid(uid: UniqueId): Manga?

    suspend fun saveManga(input: MangaInput): Manga

    suspend fun updateManga(manga: Manga, input: MangaInput): Manga

    suspend fun deleteManga(manga: Manga): Manga
}
