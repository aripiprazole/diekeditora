package com.diekeditora.infra.repositories

import com.diekeditora.domain.Entity
import com.diekeditora.domain.page.AppPage
import graphql.relay.Connection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

interface CoroutinePagingRepository<T, K, ID> : CoroutineSortingRepository<T, ID> {
    suspend fun findAll(first: Int): Flow<T>

    suspend fun findAll(first: Int, after: String): Flow<T>

    suspend fun findIndex(key: K): Long

    suspend fun estimateTotalEntries(): Long
}

internal suspend fun <T, K, ID> CoroutinePagingRepository<T, K, ID>.findPaginated(
    first: Int,
    after: String? = null,
    keySupplier: (T) -> K,
): Connection<T> where T : Any, T : Entity<T> {
    require(first > 1) { "The size of page must be bigger than 1" }
    require(first < 50) { "The size of page must be less than 50" }

    val items = if (after != null) {
        findAll(first, after).toList()
    } else {
        findAll(first).toList()
    }

    val totalItems = estimateTotalEntries()

    val firstIndex = items.firstOrNull()?.let { findIndex(keySupplier(it)) }
    val lastIndex = items.lastOrNull()?.let { findIndex(keySupplier(it)) }

    return AppPage.of(totalItems, items, first, firstIndex, lastIndex)
}
