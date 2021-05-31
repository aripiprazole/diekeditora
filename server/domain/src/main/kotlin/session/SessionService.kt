package com.lorenzoog.diekeditora.domain.session

interface SessionService {
    suspend fun validateToken(code: String, provider: SessionProvider): Session?

    suspend fun findSession(token: String): Session
}
