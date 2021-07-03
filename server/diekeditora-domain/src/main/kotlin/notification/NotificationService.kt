package com.diekeditora.domain.notification

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Page
import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow

interface NotificationService {
    suspend fun findNotifications(user: User, first: Int, after: UniqueId): Page<Notification>

    suspend fun sendNotification(user: User, notification: Notification)

    fun createSimpleNotification(message: String): SimpleNotification

    fun subscribeNotifications(user: User): Flow<Notification>
}
