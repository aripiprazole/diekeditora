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
interface UserAuthorityRepo {
    suspend fun findByUser(user: User, first: Int, after: String?): Connection<Authority>

    suspend fun findAllByUser(user: User): Set<Authority>

    suspend fun findAllByUser(user: User, first: Int, after: String?): Connection<Authority>

    suspend fun unlink(user: User, authorities: Iterable<Authority>)

    suspend fun link(user: User, authorities: Iterable<Authority>)
}

@Language("PostgreSQL")
private const val SELECT_AUTHORITIES_QUERY = """
    select * from user_authority where user_id = :user
"""

@Language("PostgreSQL")
private const val REMOVE_AUTHORITIES_QUERY = """
    delete from user_authority where user_id = :user and authority in (:authorities)
"""

@Language("PostgreSQL")
private const val CHECK_UNIQUE_AUTHORITY = """
    select * from user_authority where user_id = :user and authority_id = :authority
"""

@Language("PostgreSQL")
private const val INSERT_AUTHORITY_QUERY = """
    insert into user_authority(user_id, authority_id) values (:user, :authority)
"""

@Service
internal class UserAuthorityRepoImpl(
    val authorityRepo: AuthorityRepo,
    val template: R2dbcEntityTemplate,
) : UserAuthorityRepo {
    override suspend fun findByUser(user: User, first: Int, after: String?): Connection<Authority> {
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

        authorities
            .toSet()
            .map { authorityRepo.save(it) }
            .forEach { authority ->
                val authorityId = requireNotNull(authority.id) { "Authority id must be not null" }

                val canExecute = template.databaseClient
                    .sql(CHECK_UNIQUE_AUTHORITY)
                    .bind("user", userId.value)
                    .bind("authority", authorityId.value)
                    .fetch()
                    .flow()
                    .toList()
                    .isEmpty()

                if (canExecute) {
                    template.databaseClient
                        .sql(INSERT_AUTHORITY_QUERY)
                        .bind("user", userId.value)
                        .bind("authority", authorityId.value)
                        .await()
                }
            }
    }
}
