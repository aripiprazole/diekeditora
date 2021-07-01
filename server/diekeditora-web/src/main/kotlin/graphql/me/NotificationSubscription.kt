package com.diekeditora.web.graphql.me

import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.web.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Component

@Component
class NotificationSubscription(val notificationService: NotificationService) : Subscription {
    @GraphQLDescription("Subscribes current user for its new notifications")
    suspend fun notificationIssued(ctx: AuthGraphQLContext): Flow<Notification> {
        return notificationService.subscribeNotifications(ctx.user)
    }
}
