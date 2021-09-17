package com.diekeditora.authority.resources

import com.diekeditora.authority.domain.AuthorityService
import com.diekeditora.role.domain.Role
import com.diekeditora.role.domain.RoleService
import com.diekeditora.security.domain.Secured
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class AuthorityMutation(
    val userService: UserService,
    val roleService: RoleService,
    val authorityService: AuthorityService,
) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('authority.admin')")
    @GraphQLDescription("Attach authorities to role by its name")
    suspend fun linkAuthoritiesToRole(name: String, authorities: List<String>): Role? {
        return roleService.findRoleByName(name)?.also { target ->
            authorityService.linkAuthorities(target, authorities.toSet())
        }
    }

    @Secured
    @PreAuthorize("hasAuthority('authority.admin')")
    @GraphQLDescription("Detach authorities from role by its name")
    suspend fun unlinkAuthoritiesFromRole(name: String, authorities: List<String>): Role? {
        return roleService.findRoleByName(name)?.also { target ->
            authorityService.unlinkAuthorities(target, authorities.toSet())
        }
    }

    @Secured
    @PreAuthorize("hasAuthority('authority.admin')")
    @GraphQLDescription("Attach authorities to user by its name")
    suspend fun linkAuthoritiesToUser(username: String, authorities: List<String>): User? {
        return userService.findUserByUsername(username)?.also { target ->
            authorityService.linkAuthorities(target, authorities.toSet())
        }
    }

    @Secured
    @PreAuthorize("hasAuthority('authority.admin')")
    @GraphQLDescription("Detach authorities from user by its username")
    suspend fun unlinkAuthoritiesFromUser(username: String, authorities: List<String>): User? {
        return userService.findUserByUsername(username)?.also { target ->
            authorityService.unlinkAuthorities(target, authorities.toSet())
        }
    }
}
