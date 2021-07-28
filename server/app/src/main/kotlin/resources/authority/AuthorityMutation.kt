package com.diekeditora.app.resources.authority

import com.diekeditora.domain.authority.Authority
import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class AuthorityMutation(
    val userService: UserService,
    val roleService: RoleService,
    val authorityService: AuthorityService,
) : Mutation {
    @Secured(Authority.ADMIN)
    suspend fun linkAuthoritiesToRole(name: String, authorities: List<String>): Role? {
        return roleService.findRoleByName(name)?.also { target ->
            authorityService.linkAuthorities(target, authorities.toSet())
        }
    }

    @Secured(Authority.ADMIN)
    suspend fun unlinkAuthoritiesFromRole(name: String, authorities: List<String>): Role? {
        return roleService.findRoleByName(name)?.also { target ->
            authorityService.unlinkAuthorities(target, authorities.toSet())
        }
    }

    @Secured(Authority.ADMIN)
    suspend fun linkAuthoritiesToUser(username: String, authorities: List<String>): User? {
        return userService.findUserByUsername(username)?.also { target ->
            authorityService.linkAuthorities(target, authorities.toSet())
        }
    }

    @Secured(Authority.ADMIN)
    suspend fun unlinkAuthoritiesFromUser(username: String, authorities: List<String>): User? {
        return userService.findUserByUsername(username)?.also { target ->
            authorityService.unlinkAuthorities(target, authorities.toSet())
        }
    }
}
