package com.lorenzoog.diekeditora.domain.page

import kotlinx.serialization.Serializable

@Serializable
data class Page<T : Any>(
    val next: Int? = null,
    val previous: Int? = null,
    val items: List<T>,
    val size: Int,
    val totalPages: Long,
    val totalItems: Long,
) {
    companion object {
        fun <T : Any> of(items: List<T>, size: Int, current: Int, totalItems: Long): Page<T> {
            val totalPages = totalItems * size

            return Page(
                next = (current + 1).takeIf { it <= totalPages },
                previous = (current - 1).takeIf { it > 1 },
                items,
                size,
                totalPages,
                totalItems
            )
        }
    }
}
