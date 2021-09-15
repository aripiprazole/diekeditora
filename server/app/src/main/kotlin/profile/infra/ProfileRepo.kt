package com.diekeditora.profile.infra

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.UUID

@Repository
internal interface ProfileRepo : CoroutineSortingRepository<Profile, UUID> {
    @Query("""select * from "profile" order by created_at limit :first""")
    suspend fun findAll(first: Int): Flow<Profile>

    @Query(
        """
        select *
        from "profile"
        order by created_at
        limit :first
            offset (
                select row_number()
                over (order by created_at)
                from "profile"
                where uid = :after
            )
        """
    )
    suspend fun findAll(first: Int, after: UniqueId): Flow<Profile>

    @Query("""select row_number() over (order by created_at) from "profile" where uid = :uid limit 1""")
    suspend fun index(uid: UniqueId): BigInteger

    @Query("""select count(id) from "profile"""")
    suspend fun totalEntries(): Long

    @Query("""select * from "profile" where owner_id = :uid limit 1""")
    suspend fun findByOwnerId(uid: UUID): Profile?
}
