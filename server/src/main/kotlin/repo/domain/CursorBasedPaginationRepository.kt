package com.diekeditora.repo.domain

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

interface CursorBasedPaginationRepository<T, ID> : CoroutineSortingRepository<T, ID> {
    fun findAll(first: Int, after: String? = null, sort: Sort? = null, owner: Any? = null): Flow<T>

    suspend fun indexOf(entity: T?): Long?
}
