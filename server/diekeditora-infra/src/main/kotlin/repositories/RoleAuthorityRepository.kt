package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.UUID

@Repository
interface RoleAuthorityRepository {
    suspend fun findByRole(role: Role): Flow<Authority>

    suspend fun unlink(role: Role, authorities: Iterable<Authority>)

    suspend fun unlink(role: Role, authority: Authority): Unit = link(role, listOf(authority))

    suspend fun link(role: Role, authorities: Iterable<Authority>)

    suspend fun link(role: Role, authority: Authority): Unit = link(role, listOf(authority))
}

@Language("PostgreSQL")
private const val SELECT_ROLES_QUERY = """SELECT * FROM role_authority WHERE role_id = :role"""

@Language("PostgreSQL")
private const val REMOVE_AUTHORITIES_QUERY = """
    DELETE FROM role_authority WHERE role_id = :role AND authority IN (:authorities)
"""

@Language("PostgreSQL")
private const val CHECK_UNIQUE_AUTHORITY = """
    SELECT * FROM role_authority WHERE role_id = :role AND authority = :authority
"""

@Language("PostgreSQL")
private const val INSERT_AUTHORITY_QUERY = """
    INSERT INTO role_authority(role_id, authority) VALUES (:role, :authority)
"""

@Service
internal class RoleAuthorityRepositoryImpl(val template: R2dbcEntityTemplate) :
    RoleAuthorityRepository {
    override suspend fun findByRole(role: Role): Flow<Authority> {
        val roleId = requireNotNull(role.id) { "Role id must be not null" }

        return template.databaseClient
            .sql(SELECT_ROLES_QUERY)
            .bind("role", roleId)
            .map(template.converter.read<Authority>())
            .flow()
    }

    override suspend fun unlink(role: Role, authorities: Iterable<Authority>) {
        val roleId = requireNotNull(role.id) { "Role id must be not null" }

        template.databaseClient
            .sql(REMOVE_AUTHORITIES_QUERY)
            .bind<UUID>("role", roleId)
            .bind<Set<String>>("authorities", authorities.map(Authority::value).toSet())
            .await()
    }

    override suspend fun link(role: Role, authorities: Iterable<Authority>) {
        val roleId = requireNotNull(role.id) { "Role id must be not null" }

        authorities.toSet().forEach { (authority) ->
            val canExecute = template.databaseClient
                .sql(CHECK_UNIQUE_AUTHORITY)
                .bind("role", roleId)
                .bind("authority", authority)
                .fetch()
                .flow()
                .toList()
                .isEmpty()

            if (canExecute) {
                template.databaseClient
                    .sql(INSERT_AUTHORITY_QUERY)
                    .bind("role", roleId)
                    .bind("authority", authority)
                    .await()
            }
        }
    }
}
