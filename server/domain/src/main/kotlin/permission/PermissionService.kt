package com.diekeditora.domain.permission

import com.diekeditora.domain.user.User

interface PermissionService {
    suspend fun hasPermission(user: User, permission: String): Boolean

    suspend fun findAllPermissions(): Set<Permission>

    suspend fun createPermission(permission: String): Permission

    suspend fun deletePermission(permission: Permission)
}
