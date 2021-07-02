package com.diekeditora.domain.invoice

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Chapter
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.page.Page
import kotlinx.coroutines.flow.Flow

interface InvoiceService {
    suspend fun findAllMangaInvoices(manga: Manga, first: Int, after: UniqueId): Page<Invoice>
    suspend fun subscribeAllMangaInvoices(manga: Manga): Flow<Invoice>

    suspend fun subscribeMangaSubscriptions(manga: Manga): Flow<MangaSubscriptionInvoice>
    suspend fun findMangaSubscriptions(
        manga: Manga,
        first: Int,
        after: UniqueId
    ): Page<MangaSubscriptionInvoice>

    suspend fun subscribeChapterInvoices(chapter: Chapter): Flow<MangaChapterInvoice>
    suspend fun findChapterInvoices(
        chapter: Chapter,
        first: Int,
        after: UniqueId
    ): Page<MangaChapterInvoice>
}
