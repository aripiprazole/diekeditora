package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository

@Repository
interface RoleAuthorityRepository {
    suspend fun findByRole(role: Role): Flow<Authority>

    suspend fun save(role: Role, authority: Authority)

    suspend fun save(role: Role, authorities: Iterable<Authority>)
}

@Suppress("Detekt.FunctionNaming")
fun RoleAuthorityRepository(template: R2dbcEntityTemplate): RoleAuthorityRepository {
    return RoleAuthorityRepositoryImpl(template)
}

private class RoleAuthorityRepositoryImpl(val template: R2dbcEntityTemplate) :
    RoleAuthorityRepository {
    override suspend fun findByRole(role: Role): Flow<Authority> {
        requireNotNull(role.id) { "Role id must be not null" }

        return template.databaseClient
            .sql("""SELECT authority.* FROM authority JOIN role_authority ra on ra.role_id = :role""")
            .bind("role", role.id)
            .map { row -> template.converter.read<Authority>(row) }
            .flow()
    }

    override suspend fun save(role: Role, authority: Authority) {
        requireNotNull(role.id) { "Role id must be not null" }
        requireNotNull(authority.id) { "Authority id must be not null" }

        template.databaseClient
            .sql("""INSERT INTO role_authority(role_id, authority_id) VALUES(:role, :authority)""")
            .bind("role", role.id)
            .bind("authority", authority.id)
            .await()
    }

    // TODO FIXME
    override suspend fun save(role: Role, authorities: Iterable<Authority>) {
        authorities.forEach {
            save(role, it)
        }
    }
}
