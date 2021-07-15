package com.diekeditora.domain.page

import com.diekeditora.domain.Entity
import graphql.relay.Connection
import graphql.relay.Edge
import graphql.relay.PageInfo
import kotlin.math.round

data class AppPage<T>(
    private val edges: List<Edge<T>>,
    private val pageInfo: AppPageInfo,
) : Connection<T> {
    companion object {
        fun <T> of(
            totalItems: Long,
            items: List<T>,
            size: Int,
            firstIndex: Long?,
            lastIndex: Long?,
        ): AppPage<T> where T : Any, T : Entity<T> {
            val totalPages = round((totalItems / size).toFloat()).toInt()
            val edges = items.map { AppEdge(it, AppCursor(it.cursor)) }

            val hasNextPage = (lastIndex ?: totalItems) < totalItems
            val hasPreviousPage = (firstIndex ?: 0) > totalItems
            val startCursor = edges.firstOrNull()?.cursor
            val endCursor = edges.lastOrNull()?.cursor

            val pageInfo = AppPageInfo(
                hasNextPage, hasPreviousPage, startCursor, endCursor, totalPages
            )

            return AppPage(edges, pageInfo)
        }
    }

    override fun getEdges(): List<Edge<T>> = edges
    override fun getPageInfo(): PageInfo = pageInfo
}
