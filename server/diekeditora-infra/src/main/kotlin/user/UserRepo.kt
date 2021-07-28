package com.diekeditora.infra.user

import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.UUID

@Repository
internal interface UserRepo : CoroutineSortingRepository<User, UUID> {
    @Query("""select * from "user" order by created_at limit :first""")
    suspend fun findAll(first: Int): Flow<User>

    @Query(
        """
        select *
        from "user"
        order by created_at
        limit :first
            offset (
                select row_number()
                over (order by created_at)
                from "user"
                where username = :after
            )
        """
    )
    suspend fun findAll(first: Int, after: String): Flow<User>

    @Query("""select row_number() over (order by created_at) from "user" where username = :key limit 1""")
    suspend fun index(key: String): BigInteger

    @Query("""select count(id) from "user"""")
    suspend fun totalEntries(): Long

    @Query("""update "user" set deleted_at = current_database() where username = :username""")
    override suspend fun delete(entity: User)

    @Query("""select * from "user" where username = :username limit 1""")
    suspend fun findByUsername(username: String): User?

    @Query("""select * from "user" where email = :email limit 1""")
    suspend fun findByEmail(email: String): User?
}
