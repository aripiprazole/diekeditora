package com.diekeditora.infra.redis

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.hours

@Service
@Primary
@OptIn(ExperimentalTime::class)
internal class CachedUserService(
    val delegate: UserService,
    val redisFactory: RedisFactory,
) : CachedService, UserService by delegate {
    override val expiresIn: Duration = 1.hours

    override suspend fun findUsers(first: Int, after: String?): Connection<User> {
        return redisFactory
            .repo<Connection<User>>()
            .remember("userConnection.$first.$after", expiresIn) {
                delegate.findUsers(first, after)
            }
    }

    override suspend fun findUserByUsername(username: String): User? {
        return redisFactory
            .repo<User>()
            .remember("user.$username", expiresIn) {
                delegate.findUserByUsername(username)
            }
    }

    override suspend fun updateUser(target: User, user: User): User {
        return delegate.updateUser(target, user).also {
            redisFactory.repo<User>().delete("user.${it.username}")
        }
    }

    override suspend fun deleteUser(user: User): User {
        return delegate.deleteUser(user).also {
            redisFactory.repo<User>().delete("user.${it.username}")
        }
    }
}
