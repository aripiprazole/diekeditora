package com.diekeditora.invoice.infra

import com.diekeditora.chapter.domain.Chapter
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.invoice.domain.Invoice
import com.diekeditora.invoice.domain.InvoiceService
import com.diekeditora.invoice.domain.MangaChapterInvoice
import com.diekeditora.invoice.domain.MangaSubscriptionInvoice
import com.diekeditora.manga.domain.Manga
import com.diekeditora.page.infra.AppPage
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
internal class InvoiceServiceImpl : InvoiceService {
    override suspend fun findAllMangaInvoices(
        manga: Manga,
        first: Int,
        after: UniqueId
    ): AppPage<Invoice> {
        TODO("Not yet implemented")
    }

    override fun subscribeAllMangaInvoices(manga: Manga): Flow<Invoice> {
        TODO("Not yet implemented")
    }

    override fun subscribeMangaSubscriptions(manga: Manga): Flow<MangaSubscriptionInvoice> {
        TODO("Not yet implemented")
    }

    override suspend fun findMangaSubscriptions(
        manga: Manga,
        first: Int,
        after: UniqueId
    ): AppPage<MangaSubscriptionInvoice> {
        TODO("Not yet implemented")
    }

    override fun subscribeChapterInvoices(chapter: Chapter): Flow<MangaChapterInvoice> {
        TODO("Not yet implemented")
    }

    override suspend fun findChapterInvoices(
        chapter: Chapter,
        first: Int,
        after: UniqueId
    ): AppPage<MangaChapterInvoice> {
        TODO("Not yet implemented")
    }
}
