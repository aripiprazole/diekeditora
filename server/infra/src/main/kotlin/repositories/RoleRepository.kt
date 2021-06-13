package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoleRepository : CoroutineSortingRepository<Role, UUID> {
    @Query("""SELECT * FROM role LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)""")
    suspend fun findPaginated(page: Int, pageSize: Int = 15): Flow<Role>

    @Query("""SELECT * FROM role WHERE name = :name LIMIT 1""")
    suspend fun findByName(name: String): Role?

    @Query("""SELECT count(id) FROM role""")
    suspend fun estimateTotalRoles(): Long
}
