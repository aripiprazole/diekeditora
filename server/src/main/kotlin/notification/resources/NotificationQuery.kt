package com.diekeditora.notification.resources

import com.diekeditora.graphql.infra.AuthGraphQLContext
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.notification.domain.Notification
import com.diekeditora.notification.domain.NotificationService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.stereotype.Component

@Component
class NotificationQuery(val notificationService: NotificationService) : Query {
    @GraphQLDescription("Finds notification connection")
    suspend fun notifications(
        ctx: AuthGraphQLContext,
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("After notification id") after: UniqueId? = null,
    ): Connection<Notification> {
        return notificationService.findNotifications(ctx.user, first, after)
    }
}
