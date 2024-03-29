package com.diekeditora.shared.infra

import com.diekeditora.page.infra.AppPage
import com.diekeditora.repo.domain.CursorBasedPaginationRepository
import com.diekeditora.shared.domain.Entity
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Sort

suspend fun <T, ID> CursorBasedPaginationRepository<T, ID>.findAllAsConnection(
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
