@file:Suppress("unused")

package com.lorenzoog.diekeditora.graphql.user

import com.expediagroup.graphql.server.operations.Mutation
import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.services.UserService
import org.springframework.stereotype.Component

@Component
class UserMutation(val userService: UserService) : Mutation {
    suspend fun deleteUser(target: String) {
        val user = userService.findByUsername(target) ?: return

        userService.delete(user)
    }

    suspend fun updateUser(
        target: String,
        name: String?,
        username: String?,
        email: String?,
        password: String?,
    ): User? {
        val user = userService.findByUsername(target)
            ?.withName(name)
            ?.withUsername(username)
            ?.withEmail(email)
            ?.withPassword(password)
            ?: return null

        return userService.save(user)
    }
}
