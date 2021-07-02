package com.diekeditora.web.graphql.invoice

import com.diekeditora.domain.invoice.Invoice
import com.diekeditora.domain.invoice.InvoiceService
import com.diekeditora.domain.invoice.MangaChapterInvoice
import com.diekeditora.domain.invoice.MangaSubscriptionInvoice
import com.diekeditora.domain.manga.Chapter
import com.diekeditora.domain.manga.Manga
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Component

@Component
class InvoiceSubscription(val invoiceService: InvoiceService) : Subscription {
    @GraphQLDescription("Subscribes to manga invoice updated event")
    suspend fun mangaInvoiceUpdated(manga: Manga): Flow<Invoice> {
        return invoiceService.subscribeAllMangaInvoices(manga)
    }

    @GraphQLDescription("Subscribes to manga subscription invoice updated event")
    suspend fun mangaSubscriptionUpdated(manga: Manga): Flow<MangaSubscriptionInvoice> {
        return invoiceService.subscribeMangaSubscriptions(manga)
    }

    @GraphQLDescription("Subscribes to manga chapter invoice updated event")
    suspend fun chapterInvoiceUpdated(chapter: Chapter): Flow<MangaChapterInvoice> {
        return invoiceService.subscribeChapterInvoices(chapter)
    }
}
