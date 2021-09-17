package com.diekeditora.role.infra

import com.diekeditora.redis.domain.expiresIn
import com.diekeditora.redis.infra.CacheProvider
import com.diekeditora.role.domain.Role
import com.diekeditora.role.domain.RoleService
import com.diekeditora.user.domain.User
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Service
@Primary
@OptIn(ExperimentalTime::class)
internal class CachedRoleService(
    val delegate: RoleService,
    val cacheProvider: CacheProvider,
) : RoleService by delegate {
    override suspend fun findRoles(first: Int, after: String?): Connection<Role> {
        return cacheProvider
            .repo<Connection<Role>>()
            .remember("roleConnection.$first.$after", expiresIn) {
                delegate.findRoles(first, after)
            }
    }

    override suspend fun findRolesByUser(user: User, first: Int, after: String?): Connection<Role> {
        return cacheProvider
            .repo<Connection<Role>>()
            .remember("userRoleConnection.${user.cursor}.$first.$after", expiresIn) {
                delegate.findRolesByUser(user, first, after)
            }
    }

    override suspend fun findRoleByName(name: String): Role? {
        return cacheProvider
            .repo<Role>()
            .query("role.$name", expiresIn) {
                delegate.findRoleByName(name)
            }
    }

    override suspend fun updateRole(role: Role): Role {
        return delegate.updateRole(role).also {
            cacheProvider.repo<Role>().delete("role.${role.cursor}")
        }
    }

    override suspend fun deleteRole(role: Role) {
        return delegate.deleteRole(role).also {
            cacheProvider.repo<Role>().delete("role.${role.cursor}")
        }
    }
}
