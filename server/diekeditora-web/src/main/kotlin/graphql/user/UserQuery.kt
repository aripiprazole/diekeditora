package com.diekeditora.web.graphql.user

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserQuery(val userService: UserService) : Query {
    @GraphQLDescription("Returns user's details by its username")
    @PreAuthorize("hasAuthority('user.view')")
    suspend fun user(username: String): User? {
        return userService.findUserByUsername(username)
    }

    @GraphQLDescription("Returns user page")
    @PreAuthorize("hasAuthority('user.view')")
    suspend fun users(first: Int, after: UniqueId): Connection<User> {
        return userService.findPaginatedUsers(first, after)
    }
}
