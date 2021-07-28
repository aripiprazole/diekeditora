package com.diekeditora.web.resources.user

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserInput
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class UserMutation(private val userService: UserService) : Mutation {
    @GraphQLDescription("Creates an user with the provided data")
    @Secured(User.STORE)
    suspend fun createUser(input: UserInput): User {
        return userService.saveUser(input.toUser())
    }

    @GraphQLDescription("Updates an user by its username with the provided data")
    @Secured(User.UPDATE)
    suspend fun updateUser(username: String, input: UserInput): User? {
        val user = userService.findUserByUsername(username) ?: return null

        return userService.updateUser(user.update(input.toUser()))
    }

    @GraphQLDescription("Deletes an user by its username")
    @Secured(User.DESTROY)
    suspend fun deleteUser(username: String): User? {
        val user = userService.findUserByUsername(username) ?: return null

        return userService.deleteUser(user)
    }
}
