package com.diekeditora.infra.user

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.infra.redis.CacheProvider
import com.diekeditora.infra.redis.expiresIn
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Service
@Primary
@OptIn(ExperimentalTime::class)
internal class CachedUserService(
    val delegate: UserService,
    val cacheProvider: CacheProvider,
) : UserService by delegate {
    override suspend fun findUsers(first: Int, after: String?): Connection<User> {
        return cacheProvider
            .repo<Connection<User>>()
            .remember("userConnection.$first.$after", expiresIn) {
                delegate.findUsers(first, after)
            }
    }

    override suspend fun findUserByUsername(username: String): User? {
        return cacheProvider
            .repo<User>()
            .query("user.$username", expiresIn) {
                delegate.findUserByUsername(username)
            }
    }

    override suspend fun updateUser(user: User): User {
        return delegate.updateUser(user).also {
            cacheProvider.repo<User>().delete("user.${user.cursor}")
        }
    }

    override suspend fun deleteUser(user: User): User {
        return delegate.deleteUser(user).also {
            cacheProvider.repo<User>().delete("user.${it.username}")
        }
    }
}