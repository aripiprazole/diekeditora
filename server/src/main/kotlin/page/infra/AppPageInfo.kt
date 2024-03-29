package com.diekeditora.page.infra

import graphql.relay.ConnectionCursor
import graphql.relay.PageInfo

data class AppPageInfo(
    private val hasNextPage: Boolean,
    private val hasPreviousPage: Boolean,
    private val startCursor: ConnectionCursor?,
    private val endCursor: ConnectionCursor?,
    val totalPages: Int,
) : PageInfo {
    override fun getStartCursor(): ConnectionCursor? = startCursor
    override fun getEndCursor(): ConnectionCursor? = endCursor

    override fun isHasPreviousPage(): Boolean = hasPreviousPage
    override fun isHasNextPage(): Boolean = hasNextPage
}
