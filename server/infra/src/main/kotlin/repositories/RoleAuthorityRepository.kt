package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

@Repository
interface RoleAuthorityRepository {
    suspend fun findByRole(role: Role): Flow<Authority>

    suspend fun save(role: Role, authorities: Iterable<Authority>)

    suspend fun save(role: Role, authority: Authority): Unit = save(role, listOf(authority))
}

@Language("PostgreSQL")
private const val SELECT_ROLES_QUERY = """SELECT * FROM role_authority WHERE role_id = :role"""

@Language("PostgreSQL")
private const val INSERT_AUTHORITIES_QUERY = """
    INSERT INTO role_authority(role_id, authority)
    VALUES ($1, $2) ON CONFLICT
    DO UPDATE SET authority = excluded.authority
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

    override suspend fun save(role: Role, authorities: Iterable<Authority>) {
        val roleId = requireNotNull(role.id) { "Role id must be not null" }

        template.databaseClient.inConnectionMany { connection ->
            val statement = connection.createStatement(INSERT_AUTHORITIES_QUERY)

            authorities.forEach {
                statement.bind(0, roleId).bind(1, it.authority).add()
            }

            Flux.from(statement.execute())
        }
    }
}
