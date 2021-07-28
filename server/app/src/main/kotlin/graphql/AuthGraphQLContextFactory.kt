package com.diekeditora.app.graphql

import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContextFactory
import kotlinx.coroutines.reactive.awaitSingleOrNull
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class AuthGraphQLContextFactory(
    val converter: ServerAuthenticationConverter,
    val manager: ReactiveAuthenticationManager,
) : SpringGraphQLContextFactory<AuthGraphQLContext>() {
    @Suppress("Detekt.ReturnCount")
    override suspend fun generateContext(request: ServerRequest): AuthGraphQLContext? {
        val securityContext = ReactiveSecurityContextHolder.getContext().awaitSingleOrNull()

        if (securityContext?.authentication != null) {
            return AuthGraphQLContext(securityContext.authentication!!, request)
        }

        val token = converter.convert(request.exchange()).awaitSingleOrNull() ?: return null
        val authentication = manager.authenticate(token).awaitSingleOrNull() ?: return null

        return AuthGraphQLContext(authentication, request)
    }
}
