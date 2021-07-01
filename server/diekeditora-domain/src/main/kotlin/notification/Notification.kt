package com.diekeditora.domain.notification

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.invoice.Invoice
import com.diekeditora.domain.manga.Comment
import com.diekeditora.domain.manga.Manga
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

sealed class Notification {
    abstract val id: UniqueId?
    abstract val uid: UniqueId
    abstract val issuedAt: LocalDateTime

    open val rawContent: String? get() = null
}

data class CommentMentionNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: UniqueId? = null,
    override val uid: UniqueId,
    val comment: Comment,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
) : Notification()

data class MangaNewsletterNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: UniqueId? = null,
    override val uid: UniqueId,
    val manga: Manga,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
) : Notification()

data class MangaSelfInvoiceUpdatedNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: UniqueId? = null,
    override val uid: UniqueId,
    val invoice: Invoice,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
) : Notification()

data class MangaInvoiceUpdatedNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: UniqueId? = null,
    override val uid: UniqueId,
    val invoice: Invoice,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
) : Notification()
