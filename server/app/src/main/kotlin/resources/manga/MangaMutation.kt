package com.diekeditora.app.resources.manga

import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaInput
import com.diekeditora.domain.manga.MangaService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

@Component
class MangaMutation(val mangaService: MangaService) : Mutation {
    @Secured(Manga.STORE)
    suspend fun createManga(input: MangaInput): Manga {
        return mangaService.saveManga(input)
    }

    @Secured(Manga.UPDATE)
    suspend fun updateManga(uid: UniqueId, input: MangaInput): Manga? {
        val target = mangaService.findMangaByUid(uid) ?: return null

        return mangaService.updateManga(target, input)
    }

    @Secured(Manga.DESTROY)
    suspend fun deleteManga(uid: UniqueId): Manga? {
        val manga = mangaService.findMangaByUid(uid) ?: return null

        return mangaService.deleteManga(manga)
    }
}
