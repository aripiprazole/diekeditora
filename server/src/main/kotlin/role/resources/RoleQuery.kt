package com.diekeditora.role.resources

import com.diekeditora.role.domain.Role
import com.diekeditora.role.domain.RoleService
import com.diekeditora.security.domain.Secured
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
