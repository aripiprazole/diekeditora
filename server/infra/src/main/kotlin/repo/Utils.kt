package com.diekeditora.infra.repo

import com.diekeditora.domain.Entity
import com.diekeditora.domain.page.AppPage
import com.diekeditora.infra.utils.assertPageSize
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Sort

internal suspend fun <T, ID> CursorBasedPaginationRepository<T, ID>.findAllAsConnection(
    first: Int,
    after: String? = null,
    sort: Sort? = null
): Connection<T> where T : Any, T : Entity<T> {
    assertPageSize(first)

    val items = findAll(first, after, sort).toList()
    val totalItems = count()

    val firstIndex = indexOf(items.firstOrNull())?.toBigInteger()
    val lastIndex = indexOf(items.lastOrNull())?.toBigInteger()

    return AppPage.of(totalItems, items, first, firstIndex, lastIndex)
}
