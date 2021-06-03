package com.diekeditora.domain.user

import com.diekeditora.domain.page.Page
import com.google.firebase.auth.FirebaseToken
import org.springframework.stereotype.Service

@Service
interface UserService {
    suspend fun findUserByUsername(username: String): User?

    suspend fun findOrCreateUserByToken(token: FirebaseToken): User

    suspend fun findPaginatedUsers(page: Int = 1, pageSize: Int = 15): Page<User>

    suspend fun updateUserByUsername(username: String, user: User): User?

    suspend fun save(user: User): User

    suspend fun delete(user: User): User
}
