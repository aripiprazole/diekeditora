package com.diekeditora.infra.repositories

import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthorityRepository : CoroutineSortingRepository<Authority, UUID> {
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

    @Query("""select row_number() over (order by created_at) from "authority" where value = :value""")
    suspend fun findIndex(value: String): Long

    @Query("""select count(id) from "authority"""")
    suspend fun estimateTotalAuthorities(): Long

    @Query("""insert into authority(value) values (:authority) on conflict (authority) do nothing""")
    suspend fun save(authority: String): Boolean
}
