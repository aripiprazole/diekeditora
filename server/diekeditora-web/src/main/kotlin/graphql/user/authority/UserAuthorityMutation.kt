package com.diekeditora.web.graphql.user.authority

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserAuthorityMutation(val userService: UserService) : Mutation {
    @PreAuthorize("hasAuthority('authority.admin')")
    suspend fun linkAuthoritiesToUser(username: String, authorities: List<String>): User? {
        return userService.findUserByUsername(username)?.also { target ->
            userService.linkAuthorities(target, authorities.toSet())
        }
    }

    @PreAuthorize("hasAuthority('authority.admin')")
    suspend fun unlinkAuthoritiesFromUser(username: String, authorities: List<String>): User? {
        return userService.findUserByUsername(username)?.also { target ->
            userService.unlinkAuthorities(target, authorities.toSet())
        }
    }
}
