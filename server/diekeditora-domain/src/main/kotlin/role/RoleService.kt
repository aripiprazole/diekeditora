package com.diekeditora.domain.role

import com.diekeditora.domain.page.Page

interface RoleService {
    suspend fun findPaginatedRoles(page: Int = 1, pageSize: Int = 15): Page<Role>

    suspend fun findRoleByName(name: String): Role?

    suspend fun findRoleAuthorities(role: Role): Set<String>

    suspend fun linkAuthorities(role: Role, authorities: Set<String>)

    suspend fun unlinkAuthorities(role: Role, authorities: Set<String>)

    suspend fun save(role: Role): Role

    suspend fun update(target: Role, role: Role): Role

    suspend fun delete(role: Role)
}
