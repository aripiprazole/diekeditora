package com.diekeditora.app.page

import com.diekeditora.domain.Entity
import graphql.relay.Connection
import graphql.relay.Edge
import graphql.relay.PageInfo
import java.math.BigInteger
import kotlin.math.round

data class AppPage<T>(
    private val edges: List<Edge<T>>,
    private val pageInfo: AppPageInfo,
) : Connection<T> {
    companion object {
        fun <T> empty(): AppPage<T> {
            val pageInfo = AppPageInfo(
                hasNextPage = false,
                hasPreviousPage = false,
                startCursor = null,
                endCursor = null,
                totalPages = 0,
            )

            return AppPage(emptyList(), pageInfo)
        }

        fun <T> of(
            totalItems: Long,
            items: List<T>,
            size: Int,
            firstIndex: BigInteger?,
            lastIndex: BigInteger?,
        ): AppPage<T> where T : Any, T : Entity<T> {
            val totalPages = round((totalItems / size).toFloat()).toInt()
            val edges = items.map { AppEdge(it, AppCursor(it.cursor)) }

            val hasNextPage = (lastIndex?.toLong() ?: totalItems) < totalItems
            val hasPreviousPage = (firstIndex?.toLong() ?: 0L) > totalItems
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
