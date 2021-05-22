package com.lorenzoog.diekeditora.domain.session

interface SessionService {
    suspend fun getSession(token: String): Session?
}
