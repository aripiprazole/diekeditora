package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface AuthorityRepository {
    suspend fun findAll(): Flow<Authority>

    suspend fun findAllByUser(user: User): Flow<Authority>
}

@Language("PostgreSQL")
private const val SELECT_ALL_AUTHORITIES = """SELECT * FROM user_authority, role_authority"""

@Language("PostgreSQL")
private const val SELECT_USER_AUTHORITIES = """
    SELECT * FROM user_authority ua, role_authority ra
    LEFT JOIN role r ON ra.role_id = r.id
    RIGHT JOIN user_role ur ON ur.role_id = r.id
    WHERE ua.user_id = :user OR ur.user_id = :user
"""

@Service
internal class AuthorityRepositoryImpl(val template: R2dbcEntityTemplate) : AuthorityRepository {
    override suspend fun findAll(): Flow<Authority> {
        return template.databaseClient
            .sql(SELECT_ALL_AUTHORITIES)
            .map(template.converter.read<Authority>())
            .flow()
    }

    override suspend fun findAllByUser(user: User): Flow<Authority> {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        return template.databaseClient
            .sql(SELECT_USER_AUTHORITIES)
            .bind("user", userId)
            .map(template.converter.read<Authority>())
            .flow()
    }
}
