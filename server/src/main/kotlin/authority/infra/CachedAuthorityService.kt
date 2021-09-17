package com.diekeditora.authority.infra

import com.diekeditora.authority.domain.AuthorityService
import com.diekeditora.redis.domain.expiresIn
import com.diekeditora.redis.infra.CacheProvider
import com.diekeditora.role.domain.Role
import com.diekeditora.user.domain.User
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Service
@Primary
@OptIn(ExperimentalTime::class)
class CachedAuthorityService(
    val delegate: AuthorityService,
    val cacheProvider: CacheProvider,
) : AuthorityService by delegate {
    override suspend fun findAllAuthorities(first: Int, after: String?): Connection<String> {
        return cacheProvider
            .repo<Connection<String>>()
            .remember("authorityConnection.$first.$after", expiresIn) {
                delegate.findAllAuthorities(first, after)
            }
    }

    override suspend fun findAllAuthoritiesByUser(user: User): Set<String> {
        return cacheProvider
            .repo<Set<String>>()
            .remember("userAllAuthority.${user.cursor}", expiresIn) {
                delegate.findAllAuthoritiesByUser(user)
            }
    }

    override suspend fun findAuthoritiesByRole(
        role: Role,
        first: Int,
        after: String?
    ): Connection<String> {
        return cacheProvider
            .repo<Connection<String>>()
            .remember("roleAuthority.${role.cursor}.$first.$after", expiresIn) {
                delegate.findAuthoritiesByRole(role, first, after)
            }
    }

    override suspend fun findAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return cacheProvider
            .repo<Connection<String>>()
            .remember("userAuthority.${user.cursor}.$first.$after", expiresIn) {
                delegate.findAuthoritiesByUser(user, first, after)
            }
    }
}
