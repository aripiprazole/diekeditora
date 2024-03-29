package com.diekeditora.user.resources

import com.diekeditora.security.domain.Secured
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserInput
import com.diekeditora.user.domain.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserMutation(private val userService: UserService) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('user.store')")
    @GraphQLDescription("Creates an user with the provided data")
    suspend fun createUser(input: UserInput): User {
        return userService.saveUser(input.toUser())
    }

    @Secured
    @PreAuthorize("hasAuthority('user.update') or authentication.principal.username == #username")
    @GraphQLDescription("Updates an user by its username with the provided data")
    suspend fun updateUser(username: String, input: UserInput): User? {
        val user = userService.findUserByUsername(username) ?: return null

        return userService.updateUser(user.update(input.toUser()))
    }

    @Secured
    @PreAuthorize("hasAuthority('user.destroy')")
    @GraphQLDescription("Deletes an user by its username")
    suspend fun deleteUser(username: String) {
        val user = userService.findUserByUsername(username) ?: return

        userService.deleteUser(user)
    }
}
