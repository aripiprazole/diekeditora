package com.diekeditora.invoice.domain

import com.diekeditora.chapter.domain.Chapter
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import com.diekeditora.page.infra.AppPage
import kotlinx.coroutines.flow.Flow

interface InvoiceService {
    fun subscribeAllMangaInvoices(manga: Manga): Flow<Invoice>
    suspend fun findAllMangaInvoices(manga: Manga, first: Int, after: UniqueId): AppPage<Invoice>

    fun subscribeMangaSubscriptions(manga: Manga): Flow<MangaSubscriptionInvoice>
    suspend fun findMangaSubscriptions(
        manga: Manga,
        first: Int,
        after: UniqueId
    ): AppPage<MangaSubscriptionInvoice>

    fun subscribeChapterInvoices(chapter: Chapter): Flow<MangaChapterInvoice>
    suspend fun findChapterInvoices(
        chapter: Chapter,
        first: Int,
        after: UniqueId
    ): AppPage<MangaChapterInvoice>
}
