package com.diekeditora.graphql.infra

import com.diekeditora.Entity
import com.diekeditora.Owned
import com.diekeditora.user.domain.User
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContext
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.web.reactive.function.server.ServerRequest

class AuthGraphQLContext(val authentication: Authentication, request: ServerRequest) :
    SpringGraphQLContext(request) {
    val user: User get() = authentication.principal!! as User

    class AuthorizeContext(val user: User, val authentication: Authentication) {
        @PublishedApi
        internal val queries = mutableListOf<Boolean>()

        fun authorize(value: Boolean) {
            queries += value
        }

        fun <T> T.own(owned: Owned) where T : Any, T : Entity<T> {
            queries += owned.belongsTo(this)
        }

        fun hasAuthority(authority: String) {
            this.queries += authentication.authorities
                .map { it.authority }
                .contains(authority)
        }

        fun required(block: AuthorizeContext.() -> Unit) {
            val context = AuthorizeContext(user, authentication).apply(block)

            if (!context.queries.fold(true) { a, b -> a && b }) {
                throw AccessDeniedException("Not authorized")
            }
        }
    }

    inline operator fun invoke(block: AuthorizeContext.() -> Unit) {
        val context = AuthorizeContext(user, authentication).apply(block)

        if (!context.queries.fold(true) { a, b -> a || b }) {
            throw AccessDeniedException("Not authorized")
        }
    }
}
