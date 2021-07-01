package com.diekeditora.domain.invoice

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.user.User
import java.time.LocalDateTime

sealed class Invoice {
    abstract val id: UniqueId?
    abstract val uid: UniqueId
    abstract val payer: User
    abstract val totalValue: Int
    abstract val valuePaid: Int
    abstract val state: InvoiceState
    abstract val createdAt: LocalDateTime
    abstract val updatedAt: LocalDateTime?
}

data class MangaSubscriptionInvoice(
    override val id: UniqueId? = null,
    override val uid: UniqueId,
    override val payer: User,
    override val totalValue: Int,
    override val valuePaid: Int,
    override val state: InvoiceState,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override val updatedAt: LocalDateTime? = null,
) : Invoice()

data class MangaChapterInvoice(
    override val id: UniqueId? = null,
    override val uid: UniqueId,
    override val payer: User,
    override val totalValue: Int,
    override val valuePaid: Int,
    override val state: InvoiceState,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override val updatedAt: LocalDateTime? = null,
) : Invoice()
