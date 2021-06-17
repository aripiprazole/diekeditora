package com.diekeditora.domain.page

data class Page<T : Any>(
    val items: List<T>,
    val next: Int? = null,
    val previous: Int? = null,
    val size: Int,
    val totalPages: Long,
    val totalItems: Long,
) {
    companion object {
        fun <T : Any> of(items: List<T>, size: Int, current: Int, totalItems: Long): Page<T> {
            val totalPages = totalItems * size

            return Page(
                items,
                next = (current + 1).takeIf { it <= totalPages },
                previous = (current - 1).takeIf { it > 1 },
                size,
                totalPages,
                totalItems
            )
        }
    }
}
