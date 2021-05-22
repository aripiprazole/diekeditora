package com.lorenzoog.diekeditora.domain.session

sealed class SessionProvider

object OAuthProvider : SessionProvider()
object JwtProvider : SessionProvider()
