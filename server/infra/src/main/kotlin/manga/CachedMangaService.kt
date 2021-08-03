package com.diekeditora.infra.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.diekeditora.domain.profile.Profile
import com.diekeditora.infra.redis.CacheProvider
import com.diekeditora.infra.redis.expiresIn
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Service
@Primary
@OptIn(ExperimentalTime::class)
internal class CachedMangaService(
    val delegate: MangaService,
    val cacheProvider: CacheProvider,
) : MangaService by delegate {
    override suspend fun findMangas(
        first: Int,
        after: UniqueId?,
        orderBy: MangaSort,
        filterBy: Set<String>
    ): Connection<Manga> {
        val key = "mangaConnection.$first.$after.$orderBy.${filterBy.joinToString(".")}"

        return cacheProvider
            .repo<Connection<Manga>>()
            .remember(key, expiresIn) {
                delegate.findMangas(first, after, orderBy, filterBy)
            }
    }

    override suspend fun findAuthorsByManga(manga: Manga): List<Profile> {
        return cacheProvider
            .repo<List<Profile>>()
            .remember("profileMangaList.${manga.cursor}", expiresIn) {
                delegate.findAuthorsByManga(manga)
            }
    }

    override suspend fun deleteManga(manga: Manga): Manga {
        return delegate.deleteManga(manga).also {
            cacheProvider.repo<Manga>().delete("manga.${manga.cursor}")
        }
    }
}
