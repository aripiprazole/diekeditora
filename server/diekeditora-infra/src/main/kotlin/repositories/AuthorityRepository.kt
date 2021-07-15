package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import graphql.relay.Connection
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface AuthorityRepository {
    suspend fun findAll(first: Int, after: String?): Connection<Authority>

    suspend fun findAllByUser(user: User): Set<Authority>

    suspend fun findAllByUser(user: User, first: Int, after: String?): Connection<Authority>
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
    override suspend fun findAll(first: Int, after: String?): Connection<Authority> {
        TODO("Not yet implemented")
    }

    override suspend fun findAllByUser(user: User): Set<Authority> {
        TODO("Not yet implemented")
    }

    override suspend fun findAllByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<Authority> {
        TODO("Not yet implemented")
    }
}
