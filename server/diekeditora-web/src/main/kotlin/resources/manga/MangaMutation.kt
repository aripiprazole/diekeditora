package com.diekeditora.web.resources.manga

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class MangaMutation(val mangaService: MangaService) {
    @Secured(Manga.STORE)
    suspend fun createManga(manga: Manga): Manga {
        return mangaService.saveManga(manga)
    }

    @Secured(Manga.DESTROY)
    suspend fun updateManga(title: String, manga: Manga): Manga? {
        val target = mangaService.findMangaByTitle(title) ?: return null

        return mangaService.updateManga(target.update(manga))
    }

    @Secured(Manga.DESTROY)
    suspend fun deleteManga(title: String): Manga? {
        val manga = mangaService.findMangaByTitle(title) ?: return null

        return mangaService.deleteManga(manga)
    }
}
