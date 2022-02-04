package com.diekeditora.manga.resources

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import com.diekeditora.manga.domain.MangaInput
import com.diekeditora.manga.domain.MangaService
import com.diekeditora.security.domain.Secured
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class MangaMutation(val mangaService: MangaService) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('manga.store')")
    @GraphQLDescription("Creates a new manga with provided data")
    suspend fun createManga(input: MangaInput): Manga {
        return mangaService.saveManga(input)
    }

    @Secured
    @PreAuthorize("hasAuthority('manga.update')")
    @GraphQLDescription("Updates a role by its name with the provided data")
    suspend fun updateManga(uid: UniqueId, input: MangaInput): Manga? {
        val target = mangaService.findMangaByUid(uid) ?: return null

        return mangaService.updateManga(target, input)
    }

    @Secured
    @PreAuthorize("hasAuthority('manga.destroy')")
    @GraphQLDescription("Deletes a manga by its uid")
    suspend fun deleteManga(uid: UniqueId) {
        val manga = mangaService.findMangaByUid(uid) ?: return

        mangaService.deleteManga(manga)
    }
}
