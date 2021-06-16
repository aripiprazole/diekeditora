package com.diekeditora.web.graphql.user.authority

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserInput
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserAuthorityMutation(val userService: UserService) : Mutation {
    @PreAuthorize("hasAuthority('authority.admin')")
    suspend fun linkAuthorities(user: UserInput, authorities: List<String>): User? {
        return userService.findUserByUsername(user.username)?.also { target ->
            userService.linkAuthorities(target, authorities)
        }
    }

    @PreAuthorize("hasAuthority('authority.admin')")
    suspend fun unlinkAuthorities(user: UserInput, authorities: List<String>): User? {
        return userService.findUserByUsername(user.username)?.also { target ->
            userService.unlinkAuthorities(target, authorities)
        }
    }
}
