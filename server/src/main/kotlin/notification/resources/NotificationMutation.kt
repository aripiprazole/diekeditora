package com.diekeditora.notification.resources

import com.diekeditora.notification.domain.NotificationService
import com.diekeditora.notification.domain.SimpleNotification
import com.diekeditora.security.domain.Secured
import com.diekeditora.user.domain.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class NotificationMutation(
    val userService: UserService,
    val notificationService: NotificationService,
) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('notification.send')")
    @GraphQLDescription("Sends notification to user with username and message specified")
    suspend fun sendNotification(username: String, message: String): SimpleNotification? {
        val user = userService.findUserByUsername(username) ?: return null

        return notificationService.createSimpleNotification(message).also {
            notificationService.sendNotification(user, it)
        }
    }
}
