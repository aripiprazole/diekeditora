package com.diekeditora.app.resources.role

import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleInput
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

@Component
class RoleMutation(val userService: UserService, val roleService: RoleService) : Mutation {
    @Secured(Role.STORE)
    suspend fun createRole(input: RoleInput): Role {
        return roleService.saveRole(input.toRole())
    }

    @Secured(Role.UPDATE)
    suspend fun updateRole(name: String, input: RoleInput): Role? {
        val role = roleService.findRoleByName(name) ?: return null

        return roleService.updateRole(role.update(input.toRole()))
    }

    @Secured(Role.DESTROY)
    suspend fun deleteRole(name: String) {
        val role = roleService.findRoleByName(name) ?: return

        roleService.deleteRole(role)
    }

    @Secured(Role.ADMIN)
    suspend fun linkRolesToUser(username: String, roles: List<String>): User? {
        return userService.findUserByUsername(username)?.also { user ->
            roleService.linkRoles(user, roles.mapNotNull { roleService.findRoleByName(it) }.toSet())
        }
    }

    @Secured(Role.ADMIN)
    suspend fun unlinkRolesFromUser(username: String, roles: List<String>): User? {
        return userService.findUserByUsername(username)?.also { user ->
            roleService.unlinkRoles(
                user,
                roles.mapNotNull { roleService.findRoleByName(it) }.toSet()
            )
        }
    }
}
