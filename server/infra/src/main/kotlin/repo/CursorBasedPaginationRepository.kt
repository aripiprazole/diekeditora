package com.diekeditora.infra.repo

import com.diekeditora.domain.Entity
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository
import org.springframework.data.relational.core.query.Query.empty
import org.springframework.data.relational.core.sql.IdentifierProcessing
import org.springframework.data.relational.repository.query.RelationalEntityInformation
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.full.findAnnotation

interface ReactiveCursorBasedPaginationRepository<T, ID> : ReactiveSortingRepository<T, ID> {
    fun findAll(first: Int, after: String? = null, sort: Sort? = null): Flux<T>

    fun indexOf(entity: T?): Mono<Long>
}

interface CursorBasedPaginationRepository<T, ID> : CoroutineSortingRepository<T, ID> {
    fun findAll(first: Int, after: String? = null, sort: Sort? = null): Flow<T>

    suspend fun indexOf(entity: T?): Long?
}

@Suppress("Detekt.TooManyFunctions")
internal class ReactiveCursorBasedPaginationRepositoryImpl<T, ID>(
    val entity: RelationalEntityInformation<T, ID>,
    val operations: R2dbcEntityOperations,
    val converter: R2dbcConverter,
) : SimpleR2dbcRepository<T, ID>(entity, operations, converter),
    ReactiveCursorBasedPaginationRepository<T, ID> where T : Any, T : Entity<T> {
    override fun findAll(first: Int, after: String?, sort: Sort?): Flux<T> {
        val actualSort = sort ?: metadata.orderBy

        val query = buildString {
            append("SELECT * FROM ").append(entity.tableName.getReference(IdentifierProcessing.ANSI))
            append(" LIMIT :first ").append(actualSort.present())

            if (after != null) {
                append(" ")

                append("OFFSET (")
                append(" SELECT ROW_NUMBER() OVER (").append(actualSort.present()).append(")")
                append(" FROM ").append(entity.tableName.getReference(IdentifierProcessing.ANSI))
                append(" WHERE ").append(metadata.cursor).append(" = :after")
                append(")")
            }
        }

        return operations.databaseClient.sql(query)
            .bind("first", first)
            .run { if (after != null) bind("after", after) else this }
            .map { row, rowMetadata -> converter.read(entity.javaType, row, rowMetadata) }
            .all()
    }

    override fun indexOf(entity: T?): Mono<Long> {
        if (entity == null) return Mono.empty()

        return operations.count(empty().sort(metadata.orderBy), this.entity.javaType)
    }

    val metadata: EntityMetadata
        get() {
            val orderBy = entity.javaType.kotlin.findAnnotation<OrderBy>()
                ?: error("Could not find OrderBy annotation in entity")

            val cursor = entity.javaType.kotlin.findAnnotation<Cursor>()
                ?: error("Could not find Cursor annotation in entity")

            return EntityMetadata(Sort.by(orderBy.direction, orderBy.property), cursor.property)
        }

    private fun Sort.present(): String = buildString {
        if (isEmpty || isUnsorted) {
            error("Could not paginate items with unsorted sort")
        }

        append("ORDER BY ")

        append(this@present.toSet().joinToString { "${it.property} ${it.direction}" })
    }
}

internal data class EntityMetadata(val orderBy: Sort, val cursor: String)
