package com.diekeditora.app.page

import graphql.relay.ConnectionCursor
import graphql.relay.Edge

data class AppEdge<T>(
    private val node: T,
    private val cursor: ConnectionCursor
) : Edge<T> {
    override fun getNode(): T = node
    override fun getCursor(): ConnectionCursor = cursor
}
