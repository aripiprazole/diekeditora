package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : CrudRepository<User> {
    suspend fun findPaginatedDeleted(page: Int, pageSize: Int = 15): Flow<User>

    suspend fun findPaginated(page: Int, pageSize: Int = 15): Flow<User>

    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?

    suspend fun estimateTotalUsers(): Long
}
