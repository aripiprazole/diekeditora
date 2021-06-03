package com.diekeditora.web.graphql

import com.diekeditora.domain.user.User
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContext
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContextFactory
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.awaitSingleOrNull
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

class AuthGraphQLContext(val authentication: Authentication, request: ServerRequest) :
    SpringGraphQLContext(request) {
    val user: User get() = authentication.principal!! as User
}

@Component
class AuthGraphQLContextFactory(
    val converter: ServerAuthenticationConverter,
    val manager: ReactiveAuthenticationManager,
) : SpringGraphQLContextFactory<AuthGraphQLContext>() {
    @Suppress("Detekt.ReturnCount")
    override suspend fun generateContext(request: ServerRequest): AuthGraphQLContext? {
        val securityContext = ReactiveSecurityContextHolder.getContext().awaitSingle()

        if (securityContext.authentication != null) {
            return AuthGraphQLContext(securityContext.authentication!!, request)
        }

        val token = converter.convert(request.exchange()).awaitSingleOrNull() ?: return null
        val authentication = manager.authenticate(token).awaitSingleOrNull() ?: return null

        return AuthGraphQLContext(authentication, request)
    }
}
