package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository

@Repository
interface UserAuthorityRepository {
    suspend fun findByUser(user: User): Flow<Authority>

    suspend fun save(user: User, authority: Authority)

    suspend fun save(user: User, authorities: Iterable<Authority>)
}

@Suppress("Detekt.FunctionNaming")
fun UserAuthorityRepository(template: R2dbcEntityTemplate): UserAuthorityRepository {
    return UserAuthorityRepositoryImpl(template)
}

private class UserAuthorityRepositoryImpl(val template: R2dbcEntityTemplate) :
    UserAuthorityRepository {
    override suspend fun findByUser(user: User): Flow<Authority> {
        requireNotNull(user.id) { "User id must be not null" }

        return template.databaseClient
            .sql("""SELECT authority.* FROM authority JOIN user_authority ua on ua.user_id = :user""")
            .bind("user", user.id)
            .map { row -> template.converter.read<Authority>(row) }
            .flow()
    }

    override suspend fun save(user: User, authority: Authority) {
        requireNotNull(user.id) { "User id must be not null" }
        requireNotNull(authority.id) { "Authority id must be not null" }

        template.databaseClient
            .sql("""INSERT INTO user_authority(user_id, authority_id) VALUES(:user, :authority)""")
            .bind("user", user.id)
            .bind("authority", authority.id)
            .await()
    }

    // TODO FIXME
    override suspend fun save(user: User, authorities: Iterable<Authority>) {
        authorities.forEach {
            save(user, it)
        }
    }
}
