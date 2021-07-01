package com.diekeditora.domain.invoice

import com.diekeditora.domain.manga.Chapter
import com.diekeditora.domain.manga.Manga
import kotlinx.coroutines.flow.Flow

interface InvoiceService {
    suspend fun subscribeAllMangaInvoices(manga: Manga): Flow<Invoice>

    suspend fun subscribeMangaSubscriptions(manga: Manga): Flow<MangaSubscriptionInvoice>
    suspend fun subscribeChapterSells(chapter: Chapter): Flow<MangaChapterInvoice>
}
