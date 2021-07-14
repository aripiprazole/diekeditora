package com.diekeditora.infra.repositories

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.intellij.lang.annotations.Language
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface UserAuthorityRepository {
    suspend fun findByUser(user: User, first: Int, after: String?): Connection<Authority>

    suspend fun unlink(user: User, authorities: Iterable<Authority>)

    suspend fun unlink(user: User, authority: Authority): Unit = link(user, listOf(authority))

    suspend fun link(user: User, authorities: Iterable<Authority>)

    suspend fun link(user: User, authority: Authority): Unit = link(user, listOf(authority))
}

@Language("PostgreSQL")
private const val SELECT_AUTHORITIES_QUERY = """
    SELECT * FROM user_authority WHERE user_id = :user
"""

@Language("PostgreSQL")
private const val REMOVE_AUTHORITIES_QUERY = """
    DELETE FROM user_authority WHERE user_id = :user AND authority IN (:authorities)
"""

@Language("PostgreSQL")
private const val CHECK_UNIQUE_AUTHORITY = """
    SELECT * FROM user_authority WHERE user_id = :user AND authority = :authority
"""

@Language("PostgreSQL")
private const val INSERT_AUTHORITY_QUERY = """
    INSERT INTO user_authority(user_id, authority) VALUES (:user, :authority)
"""

@Service
internal class UserAuthorityRepositoryImpl(val template: R2dbcEntityTemplate) :
    UserAuthorityRepository {
    override suspend fun findByUser(user: User, first: Int, after: String?): Connection<Authority> {
        TODO("Not yet implemented")
    }

    override suspend fun unlink(user: User, authorities: Iterable<Authority>) {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        template.databaseClient
            .sql(REMOVE_AUTHORITIES_QUERY)
            .bind<UniqueId>("user", userId)
            .bind<Set<String>>("authorities", authorities.map(Authority::value).toSet())
            .await()
    }

    override suspend fun link(user: User, authorities: Iterable<Authority>) {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        authorities.toSet().forEach { (authority) ->
            val canExecute = template.databaseClient
                .sql(CHECK_UNIQUE_AUTHORITY)
                .bind("user", userId)
                .bind("authority", authority)
                .fetch()
                .flow()
                .toList()
                .isEmpty()

            if (canExecute) {
                template.databaseClient
                    .sql(INSERT_AUTHORITY_QUERY)
                    .bind("user", userId)
                    .bind("authority", authority)
                    .await()
            }
        }
    }
}
