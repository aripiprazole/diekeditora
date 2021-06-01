package com.diekeditora.web.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter

@Configuration
class AuthConfig {
    @Bean
    fun authenticationFilter(
        manager: ReactiveAuthenticationManager,
        converter: ServerAuthenticationConverter
    ) = AuthenticationWebFilter(manager).apply {
        setServerAuthenticationConverter(converter)
    }

    @Bean
    fun authenticationConverter(): ServerAuthenticationConverter =
        ServerBearerTokenAuthenticationConverter()
}
