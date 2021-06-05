package com.diekeditora.infra.repositories

import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.allAndAwait
import org.springframework.data.r2dbc.core.delete
import org.springframework.data.r2dbc.core.flow
import org.springframework.data.r2dbc.core.select
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.kotlin.core.publisher.toMono

@Repository
interface AuthorityRepository {
    suspend fun findAll(): Flow<Authority>

    suspend fun save(authority: Authority): Authority = saveAll(setOf(authority)).first()

    suspend fun saveAll(authorities: Iterable<Authority>): Set<Authority>

    suspend fun deleteAll(authorities: Iterable<Authority>)
}

@Suppress("Detekt.FunctionNaming")
fun AuthorityRepository(template: R2dbcEntityTemplate): AuthorityRepository =
    AuthorityRepositoryImpl(template)

private class AuthorityRepositoryImpl(val template: R2dbcEntityTemplate) : AuthorityRepository {
    override suspend fun findAll(): Flow<Authority> {
        return template.select<Authority>().flow()
    }

    override suspend fun saveAll(authorities: Iterable<Authority>): Set<Authority> {
        template.databaseClient
            .inConnection { connection ->
                val statement = connection.createStatement(
                    """
                    INSERT INTO authority(value)
                    VALUES (:value)
                    ON CONFLICT (value) DO UPDATE
                        SET value = excluded.value
                    """.trimIndent()
                )

                authorities
                    .fold(statement) { acc, next ->
                        acc.bind("value", next.value).add()
                    }
                    .execute()
                    .toMono()
                    .then()
            }
            .awaitSingle()

        return authorities.toSet()
    }

    override suspend fun deleteAll(authorities: Iterable<Authority>) {
        template
            .delete<Authority>()
            .from("authority")
            .matching(query(where("value").`in`(authorities.map(Authority::value))))
            .allAndAwait()
    }
}
