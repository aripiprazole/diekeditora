package com.diekeditora.app.role.resources

import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoleQuery(val roleService: RoleService) : Query {
    @Secured
    @PreAuthorize("hasAuthority('role.view')")
    @GraphQLDescription("Returns role details by its name")
    suspend fun role(name: String): Role? {
        return roleService.findRoleByName(name)
    }

    @Secured
    @PreAuthorize("hasAuthority('role.view')")
    @GraphQLDescription("Returns role page")
    suspend fun roles(first: Int, after: String? = null): Connection<Role> {
        return roleService.findRoles(first, after)
    }
}
