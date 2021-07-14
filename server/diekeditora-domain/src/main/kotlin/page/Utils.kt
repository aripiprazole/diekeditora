package com.diekeditora.domain.page

import graphql.relay.Connection

inline fun <T, R> Connection<T>.map(fn: (T) -> R): Connection<R> {
    return AppPage(edges.map { AppEdge(fn(it.node), it.cursor) }, pageInfo)
}
