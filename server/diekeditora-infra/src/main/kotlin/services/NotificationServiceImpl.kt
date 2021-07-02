package com.diekeditora.infra.services

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.domain.page.Page
import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
internal class NotificationServiceImpl : NotificationService {
    override suspend fun findNotifications(
        user: User,
        first: Int,
        after: UniqueId
    ): Page<Notification> {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeNotifications(user: User): Flow<Notification> {
        TODO("Not yet implemented")
    }
}
