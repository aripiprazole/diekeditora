package com.diekeditora.invoice.domain

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.shared.refs.InvoiceId
import com.diekeditora.user.domain.User
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

sealed class Invoice {
    @GraphQLIgnore
    abstract val id: InvoiceId
    abstract val uid: UniqueId
    abstract val payer: User
    abstract val totalValue: Int
    abstract val valuePaid: Int
    abstract val state: InvoiceState
    abstract val createdAt: LocalDateTime
    abstract val updatedAt: LocalDateTime?
}

data class MangaSubscriptionInvoice(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: InvoiceId = InvoiceId.New,
    override val uid: UniqueId,
    override val payer: User,
    override val totalValue: Int,
    override val valuePaid: Int,
    override val state: InvoiceState,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override val updatedAt: LocalDateTime? = null,
) : Invoice()

data class MangaChapterInvoice(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    override val id: InvoiceId = InvoiceId.New,
    override val uid: UniqueId,
    override val payer: User,
    override val totalValue: Int,
    override val valuePaid: Int,
    override val state: InvoiceState,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override val updatedAt: LocalDateTime? = null,
) : Invoice()
