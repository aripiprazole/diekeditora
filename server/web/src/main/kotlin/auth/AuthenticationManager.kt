package com.lorenzoog.diekeditora.web.auth

import com.lorenzoog.diekeditora.domain.session.SessionService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AuthenticationManager(val sessionService: SessionService) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication) = mono<Authentication> {
        println("Authentication $authentication")

        val token = authentication.principal.toString()
//        val user = sessionService.getSession(token)

        UsernamePasswordAuthenticationToken(token, null, emptyList())
    }
}
