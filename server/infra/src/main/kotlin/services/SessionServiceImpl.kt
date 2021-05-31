package com.diekeditora.infra.services

import com.diekeditora.domain.session.Session
import com.diekeditora.domain.session.SessionProvider
import com.diekeditora.domain.session.SessionService

class SessionServiceImpl : SessionService {
    override suspend fun validateToken(code: String, provider: SessionProvider): Session? {
        TODO()
    }

    override suspend fun findSession(token: String): Session {
        TODO()
    }
}
