package com.lorenzoog.diekeditora.infra.services

import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.domain.user.UserService
import org.springframework.data.domain.Page

class UserServiceImpl : UserService {
    override suspend fun findUserByUsername(username: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun findPaginatedUsers(page: Int): Page<User> {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(user: User) {
        TODO("Not yet implemented")
    }
}
