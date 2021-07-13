package com.diekeditora.domain.user

import com.google.firebase.auth.FirebaseToken
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
interface UserService {
    suspend fun findUsers(first: Int, after: String? = null): Connection<User>

    suspend fun findUserByUsername(username: String): User?

    suspend fun findOrCreateUserByToken(token: FirebaseToken): User

    suspend fun updateUser(target: User, user: User): User

    suspend fun saveUser(user: User): User

    suspend fun deleteUser(user: User): User
}
