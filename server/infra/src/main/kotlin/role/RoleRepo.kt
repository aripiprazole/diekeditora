package com.diekeditora.infra.role

import com.diekeditora.domain.role.Role
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.UUID

@Repository
internal interface RoleRepo : CoroutineSortingRepository<Role, UUID> {
    @Query("""select * from "role" order by created_at limit :first""")
    suspend fun findAll(first: Int): Flow<Role>

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
    suspend fun findAll(first: Int, after: String): Flow<Role>

    @Query("""select count(id) from "role"""")
    suspend fun totalEntries(): Long

    @Query("""select row_number() over (order by created_at) from "role" where name = :key limit 1""")
    suspend fun index(key: String): BigInteger

    @Query("""select * from "role" where name = :name limit 1""")
    suspend fun findByName(name: String): Role?
}
