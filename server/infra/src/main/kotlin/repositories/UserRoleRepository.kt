package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.utils.read
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.bind
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRepository {
    suspend fun findByUser(user: User): Flow<Role>

    suspend fun save(user: User, role: Role)

    suspend fun save(user: User, roles: Iterable<Role>)
}

@Suppress("Detekt.FunctionNaming")
fun UserRoleRepository(template: R2dbcEntityTemplate): UserRoleRepository {
    return UserRoleRepositoryImpl(template)
}

private class UserRoleRepositoryImpl(private val template: R2dbcEntityTemplate) :
    UserRoleRepository {
    override suspend fun findByUser(user: User): Flow<Role> {
        requireNotNull(user.id) { "User id must be not null" }

        return template.databaseClient
            .sql("""SELECT authority.* FROM authority JOIN user_authority ua on ua.user_id = :user""")
            .bind("user", user.id)
            .map { row -> template.converter.read<Role>(row) }
            .flow()
    }

    override suspend fun save(user: User, role: Role) {
        requireNotNull(user.id) { "User id must be not null" }
        requireNotNull(role.id) { "Role id must be not null" }

        template.databaseClient
            .sql("""INSERT INTO user_role(user_id, role_id) VALUES(:user, :role)""")
            .bind("user", user.id)
            .bind("role", role.id)
            .await()
    }

    // TODO FIXME
    override suspend fun save(user: User, roles: Iterable<Role>) {
        roles.forEach {
            save(user, it)
        }
    }
}
