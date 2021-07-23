package com.diekeditora.infra.redis

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
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

    override suspend fun updateUser(target: User, user: User): User {
        return delegate.updateUser(target, user).also {
            cacheProvider.repo<User>().delete("user.${target.username}")
        }
    }

    override suspend fun deleteUser(user: User): User {
        return delegate.deleteUser(user).also {
            cacheProvider.repo<User>().delete("user.${it.username}")
        }
    }
}
