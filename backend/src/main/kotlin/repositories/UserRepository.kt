package com.lorenzoog.diekeditora.repositories

import com.lorenzoog.diekeditora.entities.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : CoroutineSortingRepository<User, UUID> {
    @Query("""SELECT * FROM "user" WHERE deleted_at IS NULL LIMIT 15 OFFSET ((:page - 1) * 15)""")
    suspend fun findAll(@Param("page") page: Int): Flow<User>

    @Query("""SELECT * FROM "user" WHERE username = :username AND deleted_at IS NULL LIMIT 1""")
    suspend fun findByUsername(@Param("username") username: String): User?
}
