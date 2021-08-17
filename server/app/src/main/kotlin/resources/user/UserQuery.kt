package com.diekeditora.app.resources.user

import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserQuery(val userService: UserService) : Query {
    @Secured
    @PreAuthorize("hasAuthority('user.view')")
    @GraphQLDescription("Returns user details by its username")
    suspend fun user(username: String): User? {
        return userService.findUserByUsername(username)
    }

    @Secured
    @PreAuthorize("hasAuthority('user.view')")
    @GraphQLDescription("Returns user page")
    suspend fun users(first: Int, after: String? = null): Connection<User> {
        return userService.findUsers(first, after)
    }
}
