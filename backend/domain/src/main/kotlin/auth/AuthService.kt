package com.lorenzoog.diekeditora.domain.auth

import com.lorenzoog.diekeditora.domain.user.User

interface AuthService {
    suspend fun register(user: User): Session

    suspend fun login(
        username: String,
        password: String,
        provider: AuthProvider
    ): Session
}
