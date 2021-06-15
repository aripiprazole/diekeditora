package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Repository
interface UserAuthorityRepository {
    suspend fun findByUser(user: User): Flow<Authority>

    suspend fun save(user: User, authorities: Iterable<Authority>)

    suspend fun save(user: User, authority: Authority): Unit = save(user, listOf(authority))
}

@Language("PostgreSQL")
private const val SELECT_AUTHORITIES_QUERY = """
    SELECT * FROM user_authority WHERE user_id = :user
"""

@Language("PostgreSQL")
private const val INSERT_AUTHORITIES_QUERY = """
    INSERT INTO user_authority(user_id, authority)
    VALUES ($1, $2) ON CONFLICT
    DO UPDATE SET authority = excluded.authority
"""

@Service
internal class UserAuthorityRepositoryImpl(val template: R2dbcEntityTemplate) :
    UserAuthorityRepository {
    override suspend fun findByUser(user: User): Flow<Authority> {
        requireNotNull(user.id) { "User id must be not null" }

        return template.databaseClient
            .sql(SELECT_AUTHORITIES_QUERY)
            .bind("user", user.id)
            .map(template.converter.read<Authority>())
            .flow()
    }

    override suspend fun save(user: User, authorities: Iterable<Authority>) {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        template.databaseClient.inConnectionMany { connection ->
            val statement = connection.createStatement(INSERT_AUTHORITIES_QUERY)

            authorities.forEach {
                statement.bind(0, userId).bind(1, it.value).add()
            }

            Flux.from(statement.execute())
        }
    }
}
