package com.diekeditora.infra.services

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.id.UniqueIdService
import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.domain.notification.SimpleNotification
import com.diekeditora.domain.user.User
import graphql.relay.Connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.sendAndAwait
import org.springframework.stereotype.Service
import java.util.concurrent.Executors

@Service
internal class NotificationServiceImpl(
    val uniqueIdService: UniqueIdService,
    val template: ReactiveRedisTemplate<String, Notification>,
) : NotificationService, CoroutineScope {
    override val coroutineContext = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    override suspend fun findNotifications(
        user: User,
        first: Int,
        after: UniqueId?
    ): Connection<Notification> {
        TODO("Not yet implemented")
    }

    override suspend fun sendNotification(user: User, notification: Notification): Unit =
        withContext(coroutineContext) {
            template.sendAndAwait(channelNameFor(user), notification)
        }

    override fun createSimpleNotification(message: String): SimpleNotification {
        return SimpleNotification(
            uid = uniqueIdService.generateUniqueId(),
            message = message,
        )
    }

    override fun subscribeNotifications(user: User): Flow<Notification> {
        return template
            .listenToChannel(channelNameFor(user))
            .asFlow()
            .map { it.message }
    }

    private fun channelNameFor(user: User): String =
        "NOTIFICATION_USER_${user.username}"
}
