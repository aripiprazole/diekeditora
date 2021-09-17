package com.diekeditora.utils

import com.diekeditora.Entity
import com.diekeditora.com.diekeditora.repo.CursorBasedPaginationRepository
import com.diekeditora.page.infra.AppPage
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Sort

internal suspend fun <T, ID> CursorBasedPaginationRepository<T, ID>.findAllAsConnection(
    first: Int,
    after: String? = null,
    sort: Sort? = null,
    owner: Any? = null,
): Connection<T> where T : Any, T : Entity<T> {
    assertPageSize(first)

    val items = findAll(first, after, sort, owner).toList()
    val totalItems = count()

    val firstIndex = indexOf(items.firstOrNull())?.toBigInteger()
    val lastIndex = indexOf(items.lastOrNull())?.toBigInteger()

    return AppPage.of(totalItems, items, first, firstIndex, lastIndex)
}
