package com.diekeditora.infra.repositories

import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface AuthorityRepository {
    suspend fun findAll(): Flow<String>
}

@Language("PostgreSQL")
private const val FIND_ALL_STATEMENT = """SELECT user_authority.value, role_authority.value"""

@Service
internal class AuthorityRepositoryImpl : AuthorityRepository {
    override suspend fun findAll(): Flow<String> = newSuspendedTransaction {
        val authorities = exec(FIND_ALL_STATEMENT) { result ->
            flow {
                while (result.next()) {
                    emit(result.getString("value"))
                }
            }
        }

        requireNotNull(authorities)
    }
}
