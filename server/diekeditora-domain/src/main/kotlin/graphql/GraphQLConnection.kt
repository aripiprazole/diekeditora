package com.diekeditora.domain.graphql

import graphql.relay.Connection
import graphql.relay.Edge
import graphql.relay.PageInfo

data class GraphQLConnection<T>(
    private val edges: List<Edge<T>>,
    private val pageInfo: PageInfo
) : Connection<T> {
    override fun getEdges(): List<Edge<T>> = edges
    override fun getPageInfo(): PageInfo = pageInfo
}
