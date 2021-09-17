package com.diekeditora.app.me.resources

import com.diekeditora.app.notification.NotificationService
import com.diekeditora.domain.graphql.Authenticated
import com.diekeditora.domain.user.User
import com.diekeditora.infra.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class MeQuery(val notificationService: NotificationService) : Query {
    @GraphQLDescription("Returns current user context")
    @Authenticated
    fun me(ctx: AuthGraphQLContext): User {
        return ctx.user
    }

    @GraphQLDescription("Finds notification connection")
    suspend fun notifications(
        ctx: AuthGraphQLContext,
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("After notification id") after: UniqueId? = null,
    ): Connection<Notification> {
        return notificationService.findNotifications(ctx.user, first, after)
    }
}
