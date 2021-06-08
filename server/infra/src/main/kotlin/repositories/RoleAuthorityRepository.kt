package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.tables.RoleAuthority
import com.diekeditora.infra.tables.Roles
import com.diekeditora.infra.utils.insertOrUpsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.UUID

@Repository
interface RoleAuthorityRepository {
    suspend fun findByRole(target: Role): Flow<String>

    suspend fun save(target: Role, authorities: Iterable<String>)

    suspend fun save(target: Role, authority: String) {
        save(target, listOf(authority))
    }
}

private data class RoleAuthority(val value: String, val roleId: EntityID<UUID>)

@Service
internal class RoleAuthorityRepositoryImpl : RoleAuthorityRepository {
    override suspend fun findByRole(target: Role): Flow<String> = newSuspendedTransaction {
        RoleAuthority
            .select { RoleAuthority.role eq target.id }
            .asFlow()
            .map { it[RoleAuthority.authority] }
    }

    override suspend fun save(target: Role, authorities: Iterable<String>) =
        newSuspendedTransaction {
            val id = requireNotNull(target.id) { "Role id must be not null" }

            val roleId = EntityID(id, Roles)
            val keys = listOf(RoleAuthority.authority)
            val values = authorities.map { RoleAuthority(it, roleId) }

            RoleAuthority.insertOrUpsert(values, keys) { row, (value) ->
                row[authority] = value
            }
        }
}
