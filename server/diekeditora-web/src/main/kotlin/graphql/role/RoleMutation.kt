package com.diekeditora.web.graphql.role

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoleMutation(val userService: UserService, val roleService: RoleService) : Mutation {
    @PreAuthorize("hasAuthority('role.store')")
    suspend fun createRole(input: Role): Role {
        return roleService.saveRole(input)
    }

    @PreAuthorize("hasAuthority('role.update')")
    suspend fun updateRole(name: String, input: Role): Role? {
        val role = roleService.findRoleByName(name) ?: return null

        return roleService.updateRole(role, input)
    }

    @PreAuthorize("hasAuthority('role.destroy')")
    suspend fun deleteRole(name: String) {
        val role = roleService.findRoleByName(name) ?: return

        roleService.deleteRole(role)
    }

    @PreAuthorize("hasAuthority('role.admin')")
    suspend fun linkRolesToUser(username: String, roles: List<String>): User? {
        return userService.findUserByUsername(username)?.also { user ->
            roleService.linkRoles(user, roles.mapNotNull { roleService.findRoleByName(it) }.toSet())
        }
    }

    @PreAuthorize("hasAuthority('role.admin')")
    suspend fun unlinkRolesFromUser(username: String, roles: List<String>): User? {
        return userService.findUserByUsername(username)?.also { user ->
            roleService.unlinkRoles(
                user,
                roles.mapNotNull { roleService.findRoleByName(it) }.toSet()
            )
        }
    }
}
