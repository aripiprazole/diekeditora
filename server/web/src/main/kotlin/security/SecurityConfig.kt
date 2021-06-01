package com.diekeditora.web.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(val authenticationFilter: AuthenticationWebFilter) {
    private val generalCorsConfig = CorsConfiguration().apply {
        addAllowedOrigin("*")
        addAllowedMethod("*")
        addAllowedHeader("*")
    }

    @Bean
    fun securityWebFilter(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        csrf { disable() }
        httpBasic { disable() }
        formLogin { disable() }
        logout { disable() }

        addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)

        authorizeExchange {
            authorize(pathMatchers("/users", "/users/**"), hasAuthority("users.view"))
            authorize(pathMatchers(POST, "/users"), hasAuthority("users.store"))
            authorize(pathMatchers(PUT, "/users/**"), hasAuthority("users.update"))
            authorize(pathMatchers(DELETE, "/users/**"), hasAuthority("users.update"))

            authorize("/session", authenticated)
            authorize("/oauth2/authorization/*", permitAll)

            authorize(anyExchange, authenticated)
        }

        anonymous {
        }

        cors {
            configurationSource = UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("*", generalCorsConfig)
            }
        }

        exceptionHandling {
            accessDeniedHandler = ServerAccessDeniedHandler { exchange, ex ->
                exchange.response.statusCode = HttpStatus.FORBIDDEN

                Mono.empty()
            }

            authenticationEntryPoint = ServerAuthenticationEntryPoint { exchange, _ ->
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED

                Mono.empty()
            }
        }
    }
}
