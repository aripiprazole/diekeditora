package com.lorenzoog.diekeditora.dtos

import com.lorenzoog.diekeditora.entities.Entity
import kotlinx.serialization.Serializable

/**
 * Page data transfer object
 */
@Serializable
data class Page<T>(
    val totalCount: Int,
    val totalPages: Int,
    val items: List<T>,
)

fun <T : Entity> Page<T>.toEntityPage(): EntityPage {
    return EntityPage(totalCount, totalPages, items)
}

/**
 * Copy of [Page] to work with graphql, because graphql
 * has no generics, and it will be useful
 */
@Serializable
data class EntityPage(
    val totalCount: Int,
    val totalPages: Int,
    val items: List<Entity>
)
