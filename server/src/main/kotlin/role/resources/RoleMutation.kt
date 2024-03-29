package com.diekeditora.role.resources

import com.diekeditora.role.domain.Role
import com.diekeditora.role.domain.RoleInput
import com.diekeditora.role.domain.RoleService
import com.diekeditora.security.domain.Secured
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoleMutation(val userService: UserService, val roleService: RoleService) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('role.store')")
    @GraphQLDescription("Creates a new role with provided data")
    suspend fun createRole(input: RoleInput): Role {
        return roleService.saveRole(input.toRole())
    }

    @Secured
    @PreAuthorize("hasAuthority('role.update')")
    @GraphQLDescription("Updates a role by its name with the provided data")
    suspend fun updateRole(name: String, input: RoleInput): Role? {
        val role = roleService.findRoleByName(name) ?: return null

        return roleService.updateRole(role.update(input.toRole()))
    }

    @Secured
    @PreAuthorize("hasAuthority('role.destroy')")
    @GraphQLDescription("Deletes a role by its name")
    suspend fun deleteRole(name: String) {
        val role = roleService.findRoleByName(name) ?: return

        roleService.deleteRole(role)
    }

    @Secured
    @PreAuthorize("hasAuthority('role.admin')")
    @GraphQLDescription("Attach roles to user by its username")
    suspend fun linkRolesToUser(username: String, roles: List<String>): User? {
        return userService.findUserByUsername(username)?.also { user ->
            roleService.linkRoles(user, roles.mapNotNull { roleService.findRoleByName(it) }.toSet())
        }
    }

    @Secured
    @PreAuthorize("hasAuthority('role.admin')")
    @GraphQLDescription("Detach roles from user by its username")
    suspend fun unlinkRolesFromUser(username: String, roles: List<String>): User? {
        return userService.findUserByUsername(username)?.also { user ->
            roleService.unlinkRoles(
                user,
                roles.mapNotNull { roleService.findRoleByName(it) }.toSet()
            )
        }
    }
}
