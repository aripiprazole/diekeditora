package com.lorenzoog.diekeditora.domain.auth

sealed class AuthProvider

object OAuthProvider : AuthProvider()
object JwtProvider : AuthProvider()
