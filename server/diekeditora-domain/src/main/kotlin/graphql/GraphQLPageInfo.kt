package com.diekeditora.domain.graphql

import graphql.relay.ConnectionCursor
import graphql.relay.PageInfo

class GraphQLPageInfo(
    private val startCursor: ConnectionCursor,
    private val endCursor: ConnectionCursor,
    private val hasPreviousPage: Boolean,
    private val hasNextPage: Boolean,
) : PageInfo {
    override fun getStartCursor() = startCursor
    override fun getEndCursor() = endCursor
    override fun isHasPreviousPage() = hasPreviousPage
    override fun isHasNextPage() = hasNextPage
}
