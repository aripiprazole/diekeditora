package com.diekeditora.web.graphql.manga

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class MangaMutation(val mangaService: MangaService) {
    @PreAuthorize("hasAuthority('manga.store')")
    suspend fun createManga(manga: Manga): Manga {
        return mangaService.createManga(manga)
    }

    @PreAuthorize("hasAuthority('manga.update')")
    suspend fun updateManga(title: String, manga: Manga): Manga? {
        val target = mangaService.findMangaByTitle(title) ?: return null

        return mangaService.updateManga(target.update(manga))
    }

    @PreAuthorize("hasAuthority('manga.destroy')")
    suspend fun deleteManga(title: String): Manga? {
        val manga = mangaService.findMangaByTitle(title) ?: return null

        return mangaService.deleteManga(manga)
    }
}
