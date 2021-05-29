package com.lorenzoog.diekeditora.web.auth

import kotlinx.coroutines.reactor.mono
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthorizationManager : ReactiveAuthorizationManager<ServerWebExchange> {
    override fun check(auth: Mono<Authentication>, exchange: ServerWebExchange) =
        mono<AuthorizationDecision> {
            TODO()
        }
}
