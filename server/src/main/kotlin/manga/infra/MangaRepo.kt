package com.diekeditora.manga.infra

import com.diekeditora.com.diekeditora.repo.CursorBasedPaginationRepository
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import org.springframework.stereotype.Repository

@Repository
internal interface MangaRepo : CursorBasedPaginationRepository<Manga, UniqueId> {
    suspend fun findMangaByUid(uid: UniqueId): Manga?
}
