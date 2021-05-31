package com.diekeditora.domain.session

import com.diekeditora.domain.user.User

interface AuthenticationService {
    suspend fun register(user: User): Session

    suspend fun login(
        username: String,
        password: String,
        provider: SessionProvider
    ): Session
}
