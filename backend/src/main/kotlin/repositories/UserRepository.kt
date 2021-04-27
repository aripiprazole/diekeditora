package com.lorenzoog.diekeditora.repositories

import com.lorenzoog.diekeditora.entities.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CoroutineSortingRepository<User, Long> {
    @Query("SELECT * FROM users LIMIT 15 OFFSET ((:page - 1) * 15)")
    suspend fun findAll(@Param("page") page: Int): Flow<User>

    @Query("SELECT * FROM users LIMIT 1 WHERE username = :username")
    suspend fun findByUsername(@Param("id") username: String): User?
}
