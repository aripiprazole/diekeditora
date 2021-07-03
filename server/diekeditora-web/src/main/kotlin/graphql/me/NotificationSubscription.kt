package com.diekeditora.web.graphql.me

import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.web.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
@OptIn(ExperimentalCoroutinesApi::class)
class NotificationSubscription(val notificationService: NotificationService) : Subscription {
    @GraphQLDescription("Subscribes current user for its new notifications")
    fun notificationIssued(ctx: AuthGraphQLContext): Flux<Notification> =
        notificationService.subscribeNotifications(ctx.user).asFlux()
}
