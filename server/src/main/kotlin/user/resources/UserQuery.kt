package com.diekeditora.user.resources

import com.diekeditora.security.domain.Secured
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserService
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
