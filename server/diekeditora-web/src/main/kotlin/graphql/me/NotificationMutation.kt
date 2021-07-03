package com.diekeditora.web.graphql.me

import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.domain.notification.SimpleNotification
import com.diekeditora.domain.user.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class NotificationMutation(
    val userService: UserService,
    val notificationService: NotificationService,
) : Mutation {
    @PreAuthorize("hasAuthority('notification.send')")
    @GraphQLDescription("Sends notification to user with username and message specified")
    suspend fun sendNotification(username: String, message: String): SimpleNotification? {
        val user = userService.findUserByUsername(username) ?: return null

        return notificationService.createSimpleNotification(message).also {
            notificationService.sendNotification(user, it)
        }
    }
}
