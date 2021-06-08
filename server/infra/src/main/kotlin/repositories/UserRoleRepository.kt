package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.adapters.mapRole
import com.diekeditora.infra.tables.Roles
import com.diekeditora.infra.tables.UserRole
import com.diekeditora.infra.tables.Users
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
interface UserRoleRepository {
    suspend fun findByUser(target: User): Flow<Role>

    suspend fun save(target: User, roles: Iterable<Role>)

    suspend fun save(target: User, role: Role) {
        save(target, listOf(role))
    }
}

private data class UserRole(val userId: EntityID<UUID>, val roleId: EntityID<UUID>)

@Service
internal class UserRoleRepositoryImpl : UserRoleRepository {
    override suspend fun findByUser(target: User): Flow<Role> = newSuspendedTransaction {
        (UserRole innerJoin Roles)
            .select { UserRole.role eq Roles.id }
            .asFlow()
            .map(::mapRole)
    }

    override suspend fun save(target: User, roles: Iterable<Role>) = newSuspendedTransaction {
        val id = requireNotNull(target.id) { "User id must be not null" }

        val userId = EntityID(id, Users)

        val values = roles.map {
            val roleId = requireNotNull(it.id) { "Role id must be not null" }

            UserRole(userId, EntityID(roleId, Roles))
        } }
}
