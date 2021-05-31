package com.diekeditora.web.auth

import com.diekeditora.domain.session.SessionService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class AuthConverter(
    val sessionService: SessionService,
    val repository: ClientRegistrationRepository,
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange) = mono<Authentication> {
        TODO()
    }
}
