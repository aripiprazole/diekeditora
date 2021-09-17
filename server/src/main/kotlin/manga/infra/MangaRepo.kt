package com.diekeditora.manga.infra

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import org.springframework.stereotype.Repository

@Repository
internal interface MangaRepo : CursorBasedPaginationRepository<Manga, UniqueId> {
    suspend fun findMangaByUid(uid: UniqueId): Manga?
}
