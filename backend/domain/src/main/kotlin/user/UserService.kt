package com.lorenzoog.diekeditora.domain.user

import org.springframework.data.domain.Page

interface UserService {
    suspend fun findUserByUsername(username: String): User?

    suspend fun findPaginatedUsers(page: Int = 1): Page<User>

    suspend fun save(user: User)

    suspend fun delete(user: User)
}
