package com.diekeditora.web.security

import com.diekeditora.web.utils.delete
import com.diekeditora.web.utils.get
import com.diekeditora.web.utils.patch
import com.diekeditora.web.utils.post
import com.diekeditora.web.utils.put
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(val authenticationFilter: AuthenticationWebFilter) {
    private val generalCorsConfig = CorsConfiguration().apply {
        allowedOrigins = listOf("*")
        allowedMethods = listOf("*")
        allowedHeaders = listOf("*")
    }

    @Bean
    fun securityWebFilter(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        csrf { disable() }
        httpBasic { disable() }
        formLogin { disable() }
        logout { disable() }

        addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)

        authorizeExchange {
            authorize(get("/users", "/users/**"), hasAuthority("user.view"))
            authorize(post("/users"), hasAuthority("user.store"))
            authorize(put("/users/**"), hasAuthority("user.update"))
            authorize(patch("/users/**"), hasAuthority("user.update"))
            authorize(delete("/users/**"), hasAuthority("user.destroy"))

            authorize(get("/session"), authenticated)

            authorize("/oauth2/authorization/*", permitAll)

            authorize(anyExchange, permitAll)
        }

        anonymous {
        }

        cors {
            configurationSource = CorsConfigurationSource { generalCorsConfig }
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
