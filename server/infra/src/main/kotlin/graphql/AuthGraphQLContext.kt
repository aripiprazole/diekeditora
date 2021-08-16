package com.diekeditora.infra.graphql

import com.diekeditora.domain.user.User
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContext
import org.springframework.security.core.Authentication
import org.springframework.web.reactive.function.server.ServerRequest

class AuthGraphQLContext(val authentication: Authentication, request: ServerRequest) :
    SpringGraphQLContext(request) {
    val user: User get() = authentication.principal!! as User
}
