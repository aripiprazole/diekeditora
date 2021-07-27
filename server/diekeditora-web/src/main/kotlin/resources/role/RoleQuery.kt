package com.diekeditora.web.resources.role

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoleQuery(val roleService: RoleService) : Query {
    @PreAuthorize("hasAuthority('role.view')")
    suspend fun role(name: String): Role? {
        return roleService.findRoleByName(name)
    }

    @PreAuthorize("hasAuthority('role.view')")
    suspend fun roles(first: Int, after: String? = null): Connection<Role> {
        return roleService.findRoles(first, after)
    }
}
