package com.diekeditora.infra.manga

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
        after: String,
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

    override suspend fun findMangasByProfile(profile: Profile): List<Manga> {
        return cacheProvider
            .repo<List<Manga>>()
            .remember("profileMangaList.${profile.cursor}", expiresIn) {
                delegate.findMangasByProfile(profile)
            }
    }

    override suspend fun findMangaByTitle(title: String): Manga? {
        return cacheProvider
            .repo<Manga>()
            .query("manga.$title", expiresIn) {
                delegate.findMangaByTitle(title)
            }
    }

    override suspend fun updateManga(manga: Manga): Manga {
        return delegate.updateManga(manga).also {
            cacheProvider.repo<Manga>().delete("manga.${manga.cursor}")
        }
    }

    override suspend fun deleteManga(manga: Manga): Manga {
        return delegate.deleteManga(manga).also {
            cacheProvider.repo<Manga>().delete("manga.${manga.cursor}")
        }
    }
}
