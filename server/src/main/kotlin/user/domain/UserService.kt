package com.diekeditora.user.domain

import com.diekeditora.id.domain.UniqueId
import com.google.firebase.auth.FirebaseToken
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
interface UserService {
    suspend fun findUsers(first: Int, after: String? = null): Connection<User>

    suspend fun findUserById(id: UniqueId): User?

    suspend fun findUserByUsername(username: String): User?

    suspend fun findOrCreateUserByToken(token: FirebaseToken): User

    suspend fun updateUser(user: User): User

    suspend fun saveUser(user: User): User

    suspend fun deleteUser(user: User): User
}
