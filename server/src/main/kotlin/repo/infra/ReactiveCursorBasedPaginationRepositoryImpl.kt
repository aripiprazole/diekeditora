package com.diekeditora.repo.infra

import com.diekeditora.shared.domain.Entity
import com.diekeditora.id.domain.RefId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.page.domain.OrderBy
import com.diekeditora.page.domain.PaginationQuery
import com.diekeditora.repo.domain.ReactiveCursorBasedPaginationRepository
import com.diekeditora.shared.infra.findPropertiesByAnnotation
import com.diekeditora.shared.infra.findPropertyByAnnotation
import org.springframework.data.domain.Sort
import org.springframework.data.mapping.model.MutablePersistentEntity
import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository
import org.springframework.data.relational.core.mapping.BasicRelationalPersistentProperty
import org.springframework.data.relational.core.query.Query.empty
import org.springframework.data.relational.core.sql.IdentifierProcessing
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.full.findAnnotation

@Suppress("Detekt.TooManyFunctions")
class ReactiveCursorBasedPaginationRepositoryImpl<T : Entity<ID>, ID : RefId<*>>(
    val entity: MappingRelationalEntityInformation<T, ID>,
    val operations: R2dbcEntityOperations,
    val converter: R2dbcConverter,
    repositoryInterface: Class<*>,
) : SimpleR2dbcRepository<T, ID>(entity, operations, converter),
    ReactiveCursorBasedPaginationRepository<T, ID> {

    data class EntityProperties(val orderBy: Sort, val cursor: String)

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

    override fun <S : T> save(objectToSave: S): Mono<S> {
        return when (objectToSave.isNew) {
            true -> operations.insert(objectToSave)
            false -> operations.update(objectToSave)
        }
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
