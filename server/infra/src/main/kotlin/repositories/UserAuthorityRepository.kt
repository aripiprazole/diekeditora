package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import com.diekeditora.infra.tables.UserAuthority
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
interface UserAuthorityRepository {
    suspend fun findByUser(target: User): Flow<String>

    suspend fun save(target: User, authorities: Iterable<String>)

    suspend fun save(target: User, authority: String) {
        save(target, listOf(authority))
    }
}

private data class UserAuthority(val value: String, val userId: EntityID<UUID>)

@Service
internal class UserAuthorityRepositoryImpl : UserAuthorityRepository {
    override suspend fun findByUser(target: User): Flow<String> = newSuspendedTransaction {
        UserAuthority
            .select { UserAuthority.user eq target.id }
            .asFlow()
            .map { it[UserAuthority.authority] }
    }

    override suspend fun save(target: User, authorities: Iterable<String>) =
        newSuspendedTransaction {
            val id = requireNotNull(target.id) { "User id must be not null" }

            val userId = EntityID(id, Users)
            val keys = listOf(UserAuthority.authority)
            val values = authorities.map { UserAuthority(it, userId) }

            UserAuthority.insertOrUpsert(values, keys) { row, (value) ->
                row[authority] = value
            }
        }
}
