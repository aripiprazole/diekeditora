package com.lorenzoog.diekeditora.domain.session

import com.lorenzoog.diekeditora.domain.user.User

interface AuthenticationService {
    suspend fun register(user: User): Session

    suspend fun login(
        username: String,
        password: String,
        provider: SessionProvider
    ): Session
}
