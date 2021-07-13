package com.diekeditora.domain.role

import com.diekeditora.domain.user.User
import graphql.relay.Connection

interface RoleService {
    suspend fun findRoles(first: Int, after: String? = null): Connection<Role>

    suspend fun findRoleByName(name: String): Role?

    suspend fun findRolesByUser(user: User, first: Int, after: String? = null): Connection<Role>

    suspend fun linkRoles(user: User, roles: Set<Role>)

    suspend fun unlinkRoles(user: User, roles: Set<Role>)

    suspend fun save(role: Role): Role

    suspend fun update(target: Role, role: Role): Role

    suspend fun delete(role: Role)
}
