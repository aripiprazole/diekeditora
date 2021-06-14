package com.diekeditora.domain.authority

import com.diekeditora.domain.page.Page

interface RoleService {
    suspend fun findPaginatedRoles(page: Int = 1, pageSize: Int = 15): Page<Role>

    suspend fun findRoleByName(name: String): Role?

    suspend fun findRoleAuthorities(role: Role): List<String>

    suspend fun save(role: Role): Role

    suspend fun update(target: Role, role: Role): Role

    suspend fun delete(role: Role)
}
