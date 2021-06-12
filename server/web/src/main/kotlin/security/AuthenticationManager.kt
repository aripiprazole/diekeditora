package com.diekeditora.web.security

import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.await
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.reactor.mono
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component

@Component
class AuthenticationManager(
    val auth: FirebaseAuth,
    val userService: UserService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication) = mono<Authentication> {
        if (authentication !is BearerTokenAuthenticationToken) {
            throw AccessDeniedException("Invalid token type ${authentication::class.simpleName}")
        }

        val token = auth.verifyIdTokenAsync(authentication.token).await()
        val user = userService.findOrCreateUserByToken(token)

        UsernamePasswordAuthenticationToken(user, token, emptyList())
    }
}
