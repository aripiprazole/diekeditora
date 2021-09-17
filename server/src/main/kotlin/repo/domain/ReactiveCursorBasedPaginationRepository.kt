package com.diekeditora.repo.domain

import org.springframework.data.domain.Sort
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveCursorBasedPaginationRepository<T, ID> : ReactiveSortingRepository<T, ID> {
    fun findAll(first: Int, after: String? = null, sort: Sort? = null, owner: Any? = null): Flux<T>

    fun indexOf(entity: T?): Mono<Long>
}
