package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRepository {
    suspend fun findByUser(user: User): Flow<Role>

    suspend fun save(user: User, role: Role)

    suspend fun save(user: User, roles: Iterable<Role>)
}
