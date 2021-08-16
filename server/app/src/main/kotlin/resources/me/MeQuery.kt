package com.diekeditora.app.resources.me

import com.diekeditora.infra.graphql.AuthGraphQLContext
import com.diekeditora.domain.graphql.Authenticated
import com.diekeditora.domain.user.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class MeQuery : Query {
    @GraphQLDescription("Returns current user context")
    @Authenticated
    fun me(ctx: AuthGraphQLContext): User {
        return ctx.user
    }
}
