package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow

interface RoleRepository : CrudRepository<Role> {
    suspend fun findPaginated(page: Int, pageSize: Int = 15): Flow<Role>

    suspend fun findByName(name: String): Role?

    suspend fun findRoleAuthorities(role: Role): List<Authority>

    suspend fun estimateTotalRoles(): Long
}
