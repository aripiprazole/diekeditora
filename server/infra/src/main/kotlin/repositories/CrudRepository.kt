package com.diekeditora.infra.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import java.util.UUID

/**
 * Represents a crud repository in the database with entity [T]
 */
interface CrudRepository<T> {
    suspend fun findById(id: UUID): T

    suspend fun findAll(): Flow<T>

    suspend fun delete(entity: T): Unit = deleteAll(listOf(entity))

    suspend fun save(entity: T): T = saveAll(listOf(entity)).first()

    suspend fun deleteAll(entities: Iterable<T>): Unit = deleteAll(entities.asFlow())

    suspend fun deleteAll(entities: Flow<T>)

    suspend fun saveAll(entities: Iterable<T>): Flow<T> = saveAll(entities.asFlow())

    suspend fun saveAll(entities: Flow<T>): Flow<T>
}
