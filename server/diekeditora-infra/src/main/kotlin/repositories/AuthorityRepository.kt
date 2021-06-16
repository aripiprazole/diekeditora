package com.diekeditora.infra.repositories

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
}

@Language("PostgreSQL")
private const val SELECT_AUTHORITIES = """SELECT * FROM user_authority, role_authority"""

@Service
internal class AuthorityRepositoryImpl(val template: R2dbcEntityTemplate) : AuthorityRepository {
    override suspend fun findAll(): Flow<Authority> {
        return template.databaseClient
            .sql(SELECT_AUTHORITIES)
            .map(template.converter.read<Authority>())
            .flow()
    }
}
