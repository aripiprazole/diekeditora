package com.diekeditora.app.resources.invoice

import com.diekeditora.domain.invoice.InvoiceService
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component

// import com.diekeditora.domain.invoice.Invoice
// import com.diekeditora.domain.invoice.MangaChapterInvoice
// import com.diekeditora.domain.invoice.MangaSubscriptionInvoice
// import com.diekeditora.domain.chapter.Chapter
// import com.diekeditora.domain.manga.Manga
// import com.expediagroup.graphql.generator.annotations.GraphQLDescription
// import kotlinx.coroutines.reactor.asFlux
// import reactor.core.publisher.Flux

@Component
@OptIn(ExperimentalCoroutinesApi::class)
class InvoiceSubscription(val invoiceService: InvoiceService) : Subscription {
//    @GraphQLDescription("Subscribes to manga invoice updated event")
//    fun mangaInvoiceUpdated(manga: Manga): Flux<Invoice> =
//        invoiceService.subscribeAllMangaInvoices(manga).asFlux()
//
//    @GraphQLDescription("Subscribes to manga subscription invoice updated event")
//    fun mangaSubscriptionUpdated(manga: Manga): Flux<MangaSubscriptionInvoice> =
//        invoiceService.subscribeMangaSubscriptions(manga).asFlux()
//
//    @GraphQLDescription("Subscribes to manga chapter invoice updated event")
//    fun chapterInvoiceUpdated(chapter: Chapter): Flux<MangaChapterInvoice> =
//        invoiceService.subscribeChapterInvoices(chapter).asFlux()
}