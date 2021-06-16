package com.diekeditora.infra.repositories

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
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
interface UserRoleRepository {
    suspend fun findByUser(user: User): Flow<Role>

    suspend fun save(user: User, roles: Iterable<Role>)

    suspend fun save(user: User, role: Role): Unit = save(user, listOf(role))
}

@Language("PostgreSQL")
private const val SELECT_ROLES_QUERY = """
    SELECT r.* FROM role r JOIN user_role ur on ur.user_id = :user
"""

@Language("PostgreSQL")
private const val INSERT_ROLES_QUERY = """INSERT INTO user_role(user_id, role_id) VALUES ($1, $2)"""

@Service
internal class UserRoleRepositoryImpl(val template: R2dbcEntityTemplate) : UserRoleRepository {
    override suspend fun findByUser(user: User): Flow<Role> {
        requireNotNull(user.id) { "User id must be not null" }

        return template.databaseClient
            .sql(SELECT_ROLES_QUERY)
            .bind("user", user.id)
            .map(template.converter.read<Role>())
            .flow()
    }

    override suspend fun save(user: User, roles: Iterable<Role>) {
        val userId = requireNotNull(user.id) { "User id must be not null" }

        template.databaseClient.inConnectionMany { connection ->
            val statement = connection.createStatement(INSERT_ROLES_QUERY)

            roles.forEach { role ->
                val roleId = requireNotNull(role.id) { "Role id must be not null" }

                statement.bind(0, userId).bind(1, roleId).add()
            }

            Flux.from(statement.execute())
        }
    }
}
