package com.diekeditora.infra.authority

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.redis.CacheProvider
import com.diekeditora.infra.redis.expiresIn
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Service
@Primary
@OptIn(ExperimentalTime::class)
internal class CachedAuthorityService(
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
