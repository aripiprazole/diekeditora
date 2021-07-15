package com.diekeditora.domain.page

import graphql.relay.Connection

inline fun <T, R> Connection<T>.map(fn: (T) -> R): Connection<R> {
    if (pageInfo !is AppPageInfo) {
        error("all page info objects must be AppPageInfo instance")
    }

    return AppPage(edges.map { AppEdge(fn(it.node), it.cursor) }, pageInfo as AppPageInfo)
}
