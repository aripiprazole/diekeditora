package com.diekeditora.web.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authorization.AuthorizationWebFilter
import org.springframework.web.server.ServerWebExchange

@Configuration
class AuthConfig(val authenticationConverter: ServerAuthenticationConverter) {
    @Bean
    fun sessionAuthFilter(manager: ReactiveAuthenticationManager) =
        AuthenticationWebFilter(manager).apply {
            setServerAuthenticationConverter(authenticationConverter)
        }

    @Bean
    fun authorizationFilter(manager: ReactiveAuthorizationManager<ServerWebExchange>) =
        AuthorizationWebFilter(manager)
}
