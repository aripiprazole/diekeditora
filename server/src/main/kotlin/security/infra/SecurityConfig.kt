package com.diekeditora.security.infra

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    private val generalCorsConfig = CorsConfiguration().apply {
        allowedOrigins = listOf("*")
        allowedMethods = listOf("*")
        allowedHeaders = listOf("*")
    }

    @Bean
    fun authenticationConverter(): ServerAuthenticationConverter =
        ServerBearerTokenAuthenticationConverter()

    @Bean
    fun securityWebFilter(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        csrf { disable() }
        httpBasic { disable() }
        formLogin { disable() }
        logout { disable() }

        cors {
            configurationSource = CorsConfigurationSource { generalCorsConfig }
        }
    }
}
