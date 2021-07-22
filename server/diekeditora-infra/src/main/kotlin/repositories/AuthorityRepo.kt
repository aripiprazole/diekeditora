package com.diekeditora.infra.repositories

import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.UUID

@Repository
internal interface AuthorityRepo : CoroutineSortingRepository<Authority, UUID> {
    @Query("""select * from "authority" order by created_at limit :first""")
    suspend fun findAll(first: Int): Flow<Authority>

    @Query(
        """
        select *
        from "authority"
        order by created_at
        limit :first
            offset (
                select row_number()
                over (order by created_at)
                from "authority"
                where value = :after
            )
        """
    )
    suspend fun findAll(first: Int, after: String): Flow<Authority>

    @Query("""select row_number() over (order by created_at) from "authority" where value = :key limit 1""")
    suspend fun index(key: String): BigInteger

    @Query("""select count(id) from "authority"""")
    suspend fun totalEntries(): Long

    suspend fun findByValue(value: String): Authority?
}
