package com.diekeditora.infra.repo

import com.diekeditora.domain.Entity
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.diekeditora.domain.page.PaginationQuery
import com.diekeditora.shared.findPropertiesByAnnotation
import com.diekeditora.shared.findPropertyByAnnotation
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.mapping.model.MutablePersistentEntity
import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository
import org.springframework.data.relational.core.mapping.BasicRelationalPersistentProperty
import org.springframework.data.relational.core.query.Query.empty
import org.springframework.data.relational.core.sql.IdentifierProcessing
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.full.findAnnotation

interface ReactiveCursorBasedPaginationRepository<T, ID> : ReactiveSortingRepository<T, ID> {
    fun findAll(first: Int, after: String? = null, sort: Sort? = null, owner: Any? = null): Flux<T>

    fun indexOf(entity: T?): Mono<Long>
}

interface CursorBasedPaginationRepository<T, ID> : CoroutineSortingRepository<T, ID> {
    fun findAll(first: Int, after: String? = null, sort: Sort? = null, owner: Any? = null): Flow<T>

    suspend fun indexOf(entity: T?): Long?
}

@Suppress("Detekt.TooManyFunctions")
internal class ReactiveCursorBasedPaginationRepositoryImpl<T, ID>(
    val entity: MappingRelationalEntityInformation<T, ID>,
    val operations: R2dbcEntityOperations,
    val converter: R2dbcConverter,
    repositoryInterface: Class<*>,
) : SimpleR2dbcRepository<T, ID>(entity, operations, converter),
    ReactiveCursorBasedPaginationRepository<T, ID> where T : Any, T : Entity<T> {
    override fun findAll(first: Int, after: String?, sort: Sort?, owner: Any?): Flux<T> {
        val actualSort = sort ?: properties.orderBy

        val selectQuery = paginationQuery?.selectQuery ?: buildString {
            append("SELECT * FROM ").append(entity.tableName.getReference(IdentifierProcessing.ANSI))
            append(" ").append(actualSort.present())
            append(" LIMIT :first ")
        }

        val offsetQuery = paginationQuery?.offsetQuery ?: buildString {
            append("OFFSET (")
            append(" SELECT ROW_NUMBER() OVER (").append(actualSort.present()).append(")")
            append(" FROM ").append(entity.tableName.getReference(IdentifierProcessing.ANSI))
            append(" WHERE ").append(properties.cursor).append(" = :after")
            append(")")
        }

        val query = buildString {
            append(selectQuery)

            if (after != null) {
                append(" ")
                append(offsetQuery)
            }
        }

        return operations.databaseClient
            .sql(query)
            .bind("first", first)
            .run { if (after != null) bind("after", after) else this }
            .run { if (owner != null) bind("owner", owner) else this }
            .map { row, rowMetadata -> converter.read(entity.javaType, row, rowMetadata) }
            .all()
    }

    override fun indexOf(entity: T?): Mono<Long> {
        if (entity == null) return Mono.empty()

        return operations.count(empty().sort(properties.orderBy), this.entity.javaType)
    }

    val paginationQuery: PaginationQuery? = runCatching {
        repositoryInterface
            .getDeclaredMethod(
                "findAll", Int::class.java, String::class.java, Sort::class.java, Any::class.java,
            )
            .getAnnotation(PaginationQuery::class.java)
    }.getOrNull()

    val entityMetadata: MutablePersistentEntity<*, *>
        get() = entity::class.java
            .getDeclaredField("entityMetadata")
            .apply { isAccessible = true }
            .get(entity) as MutablePersistentEntity<*, *>

    val properties: EntityProperties
        get() {
            val entityMetadata = entityMetadata

            val cursor = entity.javaType.kotlin.findPropertyByAnnotation<Cursor>()
                ?.let { entityMetadata.getColumnName(it.name) }
                ?: error("Could not find Cursor annotation in entity")

            val orderBy = entity.javaType.kotlin
                .findPropertiesByAnnotation<OrderBy>()
                .map { it.findAnnotation<OrderBy>() to entityMetadata.getColumnName(it.name) }
                .mapNotNull { (orderBy, name) ->
                    if (orderBy == null) return@mapNotNull null
                    if (name == null) return@mapNotNull null

                    Sort.by(orderBy.direction, name)
                }
                .reduce { acc, sort -> acc.and(sort) }

            if (orderBy.isUnsorted || orderBy.isEmpty) {
                error("Pagination sort could not be unsorted or empty")
            }

            return EntityProperties(orderBy, cursor)
        }

    private fun MutablePersistentEntity<*, *>.getColumnName(property: String): String? {
        return (getPersistentProperty(property) as? BasicRelationalPersistentProperty)
            ?.columnName
            ?.getReference(IdentifierProcessing.ANSI)
    }

    private fun Sort.present(): String = buildString {
        if (isEmpty || isUnsorted) {
            error("Could not paginate items with unsorted sort")
        }

        append("ORDER BY ")

        append(this@present.toSet().joinToString { "${it.property} ${it.direction}" })
    }
}

internal data class EntityProperties(val orderBy: Sort, val cursor: String)
