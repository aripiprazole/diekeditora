package com.diekeditora.domain.user

import com.diekeditora.domain.page.Page
import org.springframework.stereotype.Service

@Service
interface UserService {
    suspend fun findUserByUsername(username: String): User?

    suspend fun findUserByEmail(email: String): User?

    suspend fun findPaginatedUsers(page: Int = 1, pageSize: Int = 15): Page<User>

    suspend fun updateUserByUsername(username: String, user: User): User?

    suspend fun save(user: User): User

    suspend fun delete(user: User)
}
