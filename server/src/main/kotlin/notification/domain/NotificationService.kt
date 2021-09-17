package com.diekeditora.app.notification.domain

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.user.User
import graphql.relay.Connection
import kotlinx.coroutines.flow.Flow

interface NotificationService {
    suspend fun findNotifications(
        user: User,
        first: Int,
        after: UniqueId? = null
    ): Connection<Notification>

    suspend fun sendNotification(user: User, notification: Notification)

    fun createSimpleNotification(message: String): SimpleNotification

    fun subscribeNotifications(user: User): Flow<Notification>
}
