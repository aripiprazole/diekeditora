package com.diekeditora.domain.page

import com.diekeditora.domain.Entity
import graphql.relay.Connection
import graphql.relay.Edge
import graphql.relay.PageInfo
import kotlin.math.round

data class AppPage<T>(
    private val edges: List<Edge<T>>,
    private val pageInfo: PageInfo,
) : Connection<T> {
    companion object {
        fun <T> of(
            items: List<T>,
            size: Int,
            current: Int,
            totalItems: Long
        ): AppPage<T> where T : Any, T : Entity<T> {
            val totalPages = round((totalItems / size).toFloat())
            val edges = items.map { AppEdge(it, AppCursor(it.cursor)) }

            val hasNextPage = (current + 1) <= totalPages
            val hasPreviousPage = (current - 1) <= totalPages
            val startCursor = edges.firstOrNull()?.cursor
            val endCursor = edges.lastOrNull()?.cursor

            val pageInfo = AppPageInfo(hasNextPage, hasPreviousPage, startCursor, endCursor)

            return AppPage(edges, pageInfo)
        }
    }

    inline fun <R : Any> map(fn: (Edge<T>) -> Edge<R>): AppPage<R> {
        return AppPage(getEdges().map(fn), getPageInfo())
    }

    override fun getEdges(): List<Edge<T>> = edges
    override fun getPageInfo(): PageInfo = pageInfo
}
