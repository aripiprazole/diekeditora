package com.diekeditora.security.infra

import com.diekeditora.authority.domain.AuthorityService
import com.diekeditora.shared.infra.await
import com.diekeditora.user.domain.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.reactor.mono
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component

@Component
class AuthenticationManager(
    val auth: FirebaseAuth,
    val userService: UserService,
    val authorityService: AuthorityService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication) = mono<Authentication> {
        if (authentication !is BearerTokenAuthenticationToken) {
            throw AccessDeniedException("Invalid token type ${authentication::class.simpleName}")
        }

        val token = auth.verifyIdTokenAsync(authentication.token).await()

        val user = userService.findOrCreateUserByToken(token)
        val authorities = authorityService
            .findAllAuthoritiesByUser(user)
            .map(::SimpleGrantedAuthority)

        UsernamePasswordAuthenticationToken(user, token, authorities)
    }
}
