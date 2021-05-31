package com.diekeditora.domain.permission

import com.diekeditora.domain.page.Page

interface RoleService {
    suspend fun findPaginatedRoles(page: Int = 1): Page<Role>

    suspend fun findRoleByName(name: String): Role

    suspend fun save(role: Role): Role

    suspend fun update(role: Role, target: Role): Role

    suspend fun delete(role: Role): Role
}
