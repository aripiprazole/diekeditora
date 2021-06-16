package com.diekeditora.web.graphql.role.authority

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoleAuthorityMutation(val roleService: RoleService) : Mutation {
    @PreAuthorize("hasAuthority('authority.admin')")
    suspend fun linkAuthoritiesToRole(name: String, authorities: List<String>): Role? {
        return roleService.findRoleByName(name)?.also { target ->
            roleService.linkAuthorities(target, authorities)
        }
    }

    @PreAuthorize("hasAuthority('authority.admin')")
    suspend fun unlinkAuthoritiesFromRole(name: String, authorities: List<String>): Role? {
        return roleService.findRoleByName(name)?.also { target ->
            roleService.unlinkAuthorities(target, authorities)
        }
    }
}
