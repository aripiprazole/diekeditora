package com.diekeditora.web.graphql.user

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserInput
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import kotlinx.serialization.Serializable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserMutation(private val userService: UserService) : Mutation {
    @GraphQLDescription("Creates an user with the provided data")
    @PreAuthorize("hasAuthority('users.store')")
    suspend fun createUser(input: UserInput): CreateUserPayload {
        val user = input.toUser()

        return CreateUserPayload(userService.save(user))
    }

    @GraphQLDescription("Updates an user by its username with the provided data")
    @PreAuthorize("hasAuthority('users.update')")
    suspend fun updateUser(input: UpdateUserInput): UpdateUserPayload {
        val user = userService.updateUserByUsername(input.username, input.data.toUser())

        return UpdateUserPayload(user)
    }

    @GraphQLDescription("Deletes an user by its username")
    @PreAuthorize("hasAuthority('users.destroy')")
    suspend fun deleteUser(input: DeleteUserInput): DeleteUserPayload {
        val user = userService.findUserByUsername(input.username)
            ?: return DeleteUserPayload(null)

        return DeleteUserPayload(userService.delete(user))
    }
}

@Serializable
data class DeleteUserInput(val username: String)

@Serializable
data class UpdateUserInput(val username: String, val data: UserInput)

@Serializable
data class CreateUserPayload(val user: User)

@Serializable
data class UpdateUserPayload(val user: User?)

@Serializable
data class DeleteUserPayload(val user: User?)
