package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoleRepository : CoroutineSortingRepository<Role, UUID> {
    @Query("""SELECT * FROM "user" WHERE deleted_at IS NULL LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)""")
    suspend fun findPaginated(page: Int, pageSize: Int = 15): Flow<User>

    @Query("""SELECT * FROM role_authority WHERE role_id = :roleId""")
    suspend fun findRoleAuthorities(roleId: UUID): Set<String>

    @Query("""SELECT count(id) FROM "role"""")
    suspend fun estimateTotalRoles(): Long
}
