package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepo : CoroutinePagingRepository<User, String, UUID> {
    @Query("""select * from "user" order by created_at limit :first""")
    override suspend fun findAll(first: Int): Flow<User>

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
    override suspend fun findAll(first: Int, after: String): Flow<User>

    @Query("""select row_number() over (order by created_at) from "user" where username = :username limit 1""")
    override suspend fun findIndex(username: String): Long

    @Query("""select * from "user" where username = :username limit 1""")
    suspend fun findByUsername(username: String): User?

    @Query("""select * from "user" where email = :email limit 1""")
    suspend fun findByEmail(email: String): User?

    @Query("""select count(id) from "user"""")
    suspend fun estimateTotalUsers(): Long

    @Query("""update "user" set deleted_at = current_database() where username = :username""")
    override suspend fun delete(entity: User)
}
