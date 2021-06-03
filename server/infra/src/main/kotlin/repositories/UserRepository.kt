package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : CoroutineSortingRepository<User, UUID> {
    @Query("""SELECT * FROM "user" WHERE deleted_at IS NOT NULL LIMIT :pageSize OFFSET ((:page - 1) * 15)""")
    suspend fun findAllDeleted(page: Int, pageSize: Int = 15): Flow<User>

    @Query("""SELECT * FROM "user" WHERE deleted_at IS NULL LIMIT :pageSize OFFSET ((:page - 1) * 15)""")
    suspend fun findAll(page: Int, pageSize: Int = 15): Flow<User>

    @Query("""SELECT * FROM "user" WHERE username = :username LIMIT 1""")
    suspend fun findByUsername(username: String): User?

    @Query("""SELECT * FROM "user" WHERE email = :email LIMIT 1""")
    suspend fun findByEmail(email: String): User?

    @Query("""SELECT count(id) FROM "user"""")
    suspend fun estimateTotalUsers(): Long

    @Query("""UPDATE "user" SET deleted_at = CURRENT_TIMESTAMP WHERE username = :username""")
    override suspend fun delete(entity: User)
}
