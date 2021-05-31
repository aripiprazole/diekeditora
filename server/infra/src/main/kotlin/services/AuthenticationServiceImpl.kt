package com.diekeditora.infra.services

import com.diekeditora.domain.session.AuthenticationService
import com.diekeditora.domain.session.Session
import com.diekeditora.domain.session.SessionProvider
import com.diekeditora.domain.user.User
import com.diekeditora.shared.logger

class AuthenticationServiceImpl() : AuthenticationService {
    private val log by logger()

    override suspend fun register(user: User): Session {
        log.trace("Successfully registered user %s", user)

        TODO()
    }

    override suspend fun login(
        username: String,
        password: String,
        provider: SessionProvider
    ): Session {
        TODO("Not yet implemented")
    }
}
