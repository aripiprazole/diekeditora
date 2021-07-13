package com.diekeditora.infra.services

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.invoice.Invoice
import com.diekeditora.domain.invoice.InvoiceService
import com.diekeditora.domain.invoice.MangaChapterInvoice
import com.diekeditora.domain.invoice.MangaSubscriptionInvoice
import com.diekeditora.domain.manga.Chapter
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.page.AppPage
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
