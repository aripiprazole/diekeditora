package com.diekeditora.web.graphql.me

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.web.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class NotificationQuery(val notificationService: NotificationService) : Query {
    @GraphQLDescription("Finds notification connection")
    suspend fun notifications(
        env: DataFetchingEnvironment,
        ctx: AuthGraphQLContext,
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("After notification id") after: UniqueId,
    ): Connection<Notification> {
        val (items) = notificationService.findNotifications(ctx.user, first, after)

        return SimpleListConnection(items).get(env)
    }
}
