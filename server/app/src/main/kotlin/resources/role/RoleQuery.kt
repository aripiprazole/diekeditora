package com.diekeditora.app.resources.role

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class RoleQuery(val roleService: RoleService) : Query {
    @Secured(Role.VIEW)
    suspend fun role(name: String): Role? {
        return roleService.findRoleByName(name)
    }

    @Secured(Role.VIEW)
    suspend fun roles(first: Int, after: String? = null): Connection<Role> {
        return roleService.findRoles(first, after)
    }
}
