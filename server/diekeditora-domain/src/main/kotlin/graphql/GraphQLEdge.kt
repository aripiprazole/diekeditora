package com.diekeditora.domain.graphql

import graphql.relay.ConnectionCursor
import graphql.relay.Edge

class GraphQLEdge<T>(private val node: T, private val cursor: ConnectionCursor) : Edge<T> {
    override fun getNode() = node
    override fun getCursor() = cursor
}
