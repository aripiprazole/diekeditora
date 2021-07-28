package com.diekeditora.web.resources.user

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class UserQuery(val userService: UserService) : Query {
    @GraphQLDescription("Returns user's details by its username")
    @Secured(User.VIEW)
    suspend fun user(username: String): User? {
        return userService.findUserByUsername(username)
    }

    @GraphQLDescription("Returns user page")
    @Secured(User.VIEW)
    suspend fun users(first: Int, after: String? = null): Connection<User> {
        return userService.findUsers(first, after)
    }
}
