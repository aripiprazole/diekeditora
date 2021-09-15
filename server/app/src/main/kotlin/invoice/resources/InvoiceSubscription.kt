package com.diekeditora.app.invoice.resources

import com.diekeditora.app.chapter.domain.ChapterService
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.invoice.Invoice
import com.diekeditora.domain.invoice.InvoiceService
import com.diekeditora.domain.invoice.MangaChapterInvoice
import com.diekeditora.domain.invoice.MangaSubscriptionInvoice
import com.diekeditora.domain.manga.MangaService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
@OptIn(ExperimentalCoroutinesApi::class)
class InvoiceSubscription(
    val mangaService: MangaService,
    val invoiceService: InvoiceService,
    val chapterService: ChapterService,
) : Subscription {
    @GraphQLDescription("Subscribes to manga invoice updated event")
    fun mangaInvoiceUpdated(uid: UniqueId): Flux<Invoice> =
        mono { mangaService.findMangaByUid(uid) }
            .flux()
            .flatMap { invoiceService.subscribeAllMangaInvoices(it).asFlux() }

    @GraphQLDescription("Subscribes to manga subscription invoice updated event")
    fun mangaSubscriptionUpdated(uid: UniqueId): Flux<MangaSubscriptionInvoice> =
        mono { mangaService.findMangaByUid(uid) }
            .flux()
            .flatMap { invoiceService.subscribeMangaSubscriptions(it).asFlux() }

    @GraphQLDescription("Subscribes to manga chapter invoice updated event")
    fun chapterInvoiceUpdated(uid: UniqueId): Flux<MangaChapterInvoice> =
        mono { chapterService.findChapterByUid(uid) }
            .flux()
            .flatMap { invoiceService.subscribeChapterInvoices(it).asFlux() }
}
