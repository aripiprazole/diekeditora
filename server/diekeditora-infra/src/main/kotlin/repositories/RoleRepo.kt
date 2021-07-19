package com.diekeditora.infra.repositories

import com.diekeditora.domain.role.Role
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoleRepo : CoroutinePagingRepository<Role, String, UUID> {
    @Query("""select * from "role" order by created_at limit :first""")
    override suspend fun findAll(first: Int): Flow<Role>

    @Query(
        """
        select *
        from "role"
        order by created_at
        limit :first
            offset (
                select row_number()
                over (order by created_at)
                from "role"
                where name = :after
            )
        """
    )
    override suspend fun findAll(first: Int, after: String): Flow<Role>

    @Query("""select row_number() over (order by created_at) from "role" where name = :name limit 1""")
    override suspend fun findIndex(name: String): Long

    @Query("""select * from "role" where name = :name limit 1""")
    suspend fun findByName(name: String): Role?

    @Query("""select count(id) from "role"""")
    suspend fun estimateTotalRoles(): Long
}
