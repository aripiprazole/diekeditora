package com.diekeditora.infra.services

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.profile.Profile
import org.springframework.stereotype.Service

@Service
internal class MangaServiceImpl : MangaService {
    override suspend fun findMangas(
        first: Int,
        after: String,
        orderBy: MangaSort,
        filterBy: Set<String>
    ): AppPage<Manga> {
        TODO("Not yet implemented")
    }

    override suspend fun findMangaByTitle(title: String): Manga? {
        TODO("Not yet implemented")
    }

    override suspend fun saveManga(manga: Manga): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun updateManga(manga: Manga): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun deleteManga(manga: Manga): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun findMangasByProfile(
        profile: Profile,
        first: Int,
        after: String?,
        orderBy: MangaSort,
        filterBy: Set<String>
    ): List<Manga> {
        TODO("Not yet implemented")
    }
}
