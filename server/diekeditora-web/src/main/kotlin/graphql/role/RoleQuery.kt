package com.diekeditora.web.graphql.role

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironment
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoleQuery(val roleService: RoleService) : Query {
    @PreAuthorize("hasAuthority('role.view')")
    suspend fun role(name: String): Role? {
        return roleService.findRoleByName(name)
    }

    @PreAuthorize("hasAuthority('role.view')")
    suspend fun roles(page: Int, env: DataFetchingEnvironment): Connection<Role> {
        val (items) = roleService.findRoles(page)

        return SimpleListConnection(items).get(env)
    }
}
