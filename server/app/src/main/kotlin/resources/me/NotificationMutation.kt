package com.diekeditora.app.resources.me

import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.domain.notification.SimpleNotification
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

@Component
class NotificationMutation(
    val userService: UserService,
    val notificationService: NotificationService,
) : Mutation {
    @GraphQLDescription("Sends notification to user with username and message specified")
    @Secured(Notification.SEND)
    suspend fun sendNotification(username: String, message: String): SimpleNotification? {
        val user = userService.findUserByUsername(username) ?: return null

        return notificationService.createSimpleNotification(message).also {
            notificationService.sendNotification(user, it)
        }
    }
}
