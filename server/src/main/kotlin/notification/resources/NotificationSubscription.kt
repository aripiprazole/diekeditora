package com.diekeditora.notification.resources

import com.diekeditora.graphql.infra.AuthGraphQLContext
import com.diekeditora.notification.domain.Notification
import com.diekeditora.notification.domain.NotificationService
import com.diekeditora.security.domain.Authenticated
import com.diekeditora.security.domain.Secured
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
@OptIn(ExperimentalCoroutinesApi::class)
class NotificationSubscription(val notificationService: NotificationService) : Subscription {
    @Secured
    @Authenticated
    @GraphQLDescription("Subscribes current user for its new notifications")
    fun notificationReceived(ctx: AuthGraphQLContext): Flux<Notification> =
        notificationService.subscribeNotifications(ctx.user).asFlux()
}
