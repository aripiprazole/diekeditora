package com.lorenzoog.diekeditora.infra.services

import com.lorenzoog.diekeditora.domain.session.Session
import com.lorenzoog.diekeditora.domain.session.SessionProvider
import com.lorenzoog.diekeditora.domain.session.SessionService

class SessionServiceImpl : SessionService {
    override suspend fun validateToken(code: String, provider: SessionProvider): Session? {
        TODO()
    }

    override suspend fun findSession(token: String): Session {
        TODO()
    }
}
