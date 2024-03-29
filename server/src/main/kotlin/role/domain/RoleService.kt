package com.diekeditora.role.domain

import com.diekeditora.user.domain.User
import graphql.relay.Connection

interface RoleService {
    suspend fun findRoles(first: Int, after: String? = null): Connection<Role>

    suspend fun findRoleByName(name: String): Role?

    suspend fun findRolesByUser(user: User, first: Int, after: String? = null): Connection<Role>

    suspend fun linkRoles(user: User, roles: Set<Role>)

    suspend fun unlinkRoles(user: User, roles: Set<Role>)

    suspend fun saveRole(role: Role): Role

    suspend fun updateRole(role: Role): Role

    suspend fun deleteRole(role: Role)

    suspend fun userHasRole(user: User, role: Role): Boolean
}
