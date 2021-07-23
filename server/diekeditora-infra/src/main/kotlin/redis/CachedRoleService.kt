package com.diekeditora.infra.redis

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
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
