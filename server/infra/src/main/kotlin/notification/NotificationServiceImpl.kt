package com.diekeditora.infra.notification

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.id.UniqueIdService
import com.diekeditora.domain.notification.Notification
import com.diekeditora.domain.notification.NotificationService
import com.diekeditora.domain.notification.SimpleNotification
import com.diekeditora.domain.user.User
import com.diekeditora.infra.redis.CacheProvider
import graphql.relay.Connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import org.springframework.data.redis.core.sendAndAwait
import org.springframework.stereotype.Service
import java.util.concurrent.Executors

@Service
internal class NotificationServiceImpl(
    val cacheProvider: CacheProvider,
    val uniqueIdService: UniqueIdService,
) : NotificationService, CoroutineScope {
    override val coroutineContext = Executors
        .newFixedThreadPool(NOTIFICATION_POOL_SIZE)
        .asCoroutineDispatcher()

    override suspend fun findNotifications(
        user: User,
        first: Int,
        after: UniqueId?
    ): Connection<Notification> {
        TODO("Not yet implemented")
    }

    override suspend fun sendNotification(user: User, notification: Notification): Unit =
        withContext(coroutineContext) {
            cacheProvider
                .template<Notification>()
                .sendAndAwait(channelNameFor(user), notification)
        }

    override fun createSimpleNotification(message: String): SimpleNotification {
        return SimpleNotification(
            uid = uniqueIdService.generateUniqueId(),
            message = message,
        )
    }

    override fun subscribeNotifications(user: User): Flow<Notification> {
        return cacheProvider
            .template<Notification>()
            .listenToChannel(channelNameFor(user))
            .asFlow()
            .map { it.message }
    }

    private fun channelNameFor(user: User): String =
        "NOTIFICATION_USER_${user.username}"

    companion object {
        private const val NOTIFICATION_POOL_SIZE = 4
    }
}