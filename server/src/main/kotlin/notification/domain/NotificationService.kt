package com.diekeditora.notification.domain

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.user.domain.User
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
