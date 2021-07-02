package com.diekeditora.infra.services

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.diekeditora.domain.page.Page
import org.springframework.stereotype.Service

@Service
internal class MangaServiceImpl : MangaService {
    override suspend fun findMangas(
        first: Int,
        after: String,
        orderBy: MangaSort,
        filterBy: Set<String>
    ): Page<Manga> {
        TODO("Not yet implemented")
    }

    override suspend fun findMangaByTitle(title: String): Manga? {
        TODO("Not yet implemented")
    }

    override suspend fun createManga(manga: Manga): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun updateManga(manga: Manga): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun deleteManga(manga: Manga): Manga {
        TODO("Not yet implemented")
    }
}
