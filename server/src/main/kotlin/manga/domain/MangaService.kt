package com.diekeditora.manga.domain

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.profile.domain.Profile
import com.diekeditora.user.domain.User
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

    suspend fun deleteManga(manga: Manga)
}
