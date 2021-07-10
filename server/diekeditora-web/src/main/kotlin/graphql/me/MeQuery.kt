package com.diekeditora.web.graphql.me

import com.diekeditora.domain.user.User
import com.diekeditora.web.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class MeQuery : Query {
    @GraphQLDescription("Returns current user context")
    @PreAuthorize("isAuthenticated()")
    fun me(ctx: AuthGraphQLContext): User {
        return ctx.user
    }
}
