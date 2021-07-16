package com.diekeditora.infra.repositories

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.role.Role
import com.diekeditora.infra.entities.Authority
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
interface RoleAuthorityRepo {
    suspend fun findByRole(role: Role, first: Int, after: String?): Connection<Authority>

    suspend fun unlink(role: Role, authorities: Iterable<Authority>)

    suspend fun link(role: Role, authorities: Iterable<Authority>)
}

@Language("PostgreSQL")
private const val SELECT_ROLES_QUERY = """SELECT * FROM role_authority WHERE role_id = :role"""

@Language("PostgreSQL")
private const val REMOVE_AUTHORITIES_QUERY = """
    DELETE FROM role_authority WHERE role_id = :role AND authority_id IN (:authorities)
"""

@Language("PostgreSQL")
private const val CHECK_UNIQUE_AUTHORITY = """
    SELECT * FROM role_authority WHERE role_id = :role AND authority_id = :authority
"""

@Language("PostgreSQL")
private const val INSERT_AUTHORITY_QUERY = """
    INSERT INTO role_authority(role_id, authority_id) VALUES (:role, :authority)
"""

@Service
internal class RoleAuthorityRepoImpl(
    val authorityRepo: AuthorityRepo,
    val template: R2dbcEntityTemplate,
) : RoleAuthorityRepo {
    override suspend fun findByRole(role: Role, first: Int, after: String?): Connection<Authority> {
        TODO("Not yet implemented")
    }

    override suspend fun unlink(role: Role, authorities: Iterable<Authority>) {
        val roleId = requireNotNull(role.id) { "Role id must be not null" }

        template.databaseClient
            .sql(REMOVE_AUTHORITIES_QUERY)
            .bind<UniqueId>("role", roleId)
            .bind<Set<String>>("authorities", authorities.map(Authority::value).toSet())
            .await()
    }

    override suspend fun link(role: Role, authorities: Iterable<Authority>) {
        val roleId = requireNotNull(role.id) { "Role id must be not null" }

        authorities.toSet().forEach { (authorityId, value) ->
            authorityRepo.save(value)

            val canExecute = template.databaseClient
                .sql(CHECK_UNIQUE_AUTHORITY)
                .bind("role", roleId)
                .bind("authority", authorityId)
                .fetch()
                .flow()
                .toList()
                .isEmpty()

            if (canExecute) {
                template.databaseClient
                    .sql(INSERT_AUTHORITY_QUERY)
                    .bind("role", roleId)
                    .bind("authority", authorityId)
                    .await()
            }
        }
    }
}