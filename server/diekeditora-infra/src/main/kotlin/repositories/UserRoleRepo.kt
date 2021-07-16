package com.diekeditora.infra.repositories

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface UserRoleRepo {
    suspend fun findByUser(user: User): Connection<Role>

    suspend fun unlink(user: User, roles: Iterable<Role>)

    suspend fun unlink(user: User, role: Role): Unit = link(user, listOf(role))

    suspend fun link(user: User, roles: Iterable<Role>)

    suspend fun link(user: User, role: Role): Unit = link(user, listOf(role))
}

@Language("PostgreSQL")
private const val SELECT_ROLES_QUERY = """
    SELECT r.* FROM role r JOIN user_role ur ON ur.user_id = :user AND r.id = ur.role_id
"""

@Language("PostgreSQL")
private const val REMOVE_ROLES_QUERY = """
    DELETE FROM user_role WHERE user_id = :user AND role_id IN (:roles)
"""

@Language("PostgreSQL")
private const val CHECK_UNIQUE_ROLE = """
    SELECT * FROM user_role WHERE user_id = :user AND role_id = :role
"""

@Language("PostgreSQL")
private const val INSERT_ROLE_QUERY = """
    INSERT INTO user_role(user_id, role_id) VALUES (:user, :role)
"""

@Service
internal class UserRoleRepoImpl(val template: R2dbcEntityTemplate) : UserRoleRepo {
    override suspend fun findByUser(user: User): Connection<Role> {
        TODO()
    }

    override suspend fun unlink(user: User, roles: Iterable<Role>) {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        val roleIds = roles.map { role ->
            requireNotNull(role.id) { "Role id must be not null" }
        }

        template.databaseClient
            .sql(REMOVE_ROLES_QUERY)
            .bind<UniqueId>("user", userId)
            .bind<Set<UniqueId>>("roles", roleIds.toSet())
            .await()
    }

    override suspend fun link(user: User, roles: Iterable<Role>) {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        roles.toSet().forEach { role ->
            val roleId = requireNotNull(role.id) { "Role id must be not null" }

            val canExecute = template.databaseClient
                .sql(CHECK_UNIQUE_ROLE)
                .bind("user", userId)
                .bind("role", roleId)
                .fetch()
                .flow()
                .toList()
                .isEmpty()

            if (canExecute) {
                template.databaseClient
                    .sql(INSERT_ROLE_QUERY)
                    .bind("user", userId)
                    .bind("role", roleId)
                    .await()
            }
        }
    }
}
