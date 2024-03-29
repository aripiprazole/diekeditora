package com.diekeditora.notification.domain

import com.diekeditora.comment.domain.Comment
import com.diekeditora.database.domain.NotificationId
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.invoice.domain.Invoice
import com.diekeditora.manga.domain.Manga
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
sealed class Notification {
    @GraphQLIgnore
    abstract val id: NotificationId
    abstract val uid: UniqueId
    abstract val issuedAt: LocalDateTime
    abstract val readAt: LocalDateTime?

    companion object Permissions {
        const val SEND = "notification.send"
    }
}

@JsonTypeName("CommentMentionNotification")
data class CommentMentionNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: NotificationId = NotificationId.New,
    override val uid: UniqueId,
    val comment: Comment,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
    override val readAt: LocalDateTime? = null,
) : Notification()

@JsonTypeName("MangaNewsletterNotification")
data class MangaNewsletterNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: NotificationId = NotificationId.New,
    override val uid: UniqueId,
    val manga: Manga,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
    override val readAt: LocalDateTime? = null,
) : Notification()

@JsonTypeName("MangaSelfInvoiceUpdatedNotification")
data class MangaSelfInvoiceUpdatedNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: NotificationId = NotificationId.New,
    override val uid: UniqueId,
    val invoice: Invoice,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
    override val readAt: LocalDateTime? = null,
) : Notification()

@JsonTypeName("MangaInvoiceUpdatedNotification")
data class MangaInvoiceUpdatedNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: NotificationId = NotificationId.New,
    override val uid: UniqueId,
    val invoice: Invoice,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
    override val readAt: LocalDateTime? = null,
) : Notification()

@JsonTypeName("SimpleNotification")
data class SimpleNotification(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: NotificationId = NotificationId.New,
    override val uid: UniqueId,
    val message: String,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
    override val readAt: LocalDateTime? = null,
) : Notification()
