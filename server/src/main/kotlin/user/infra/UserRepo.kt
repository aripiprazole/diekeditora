package com.diekeditora.user.infra

import com.diekeditora.repo.domain.CursorBasedPaginationRepository
import com.diekeditora.user.domain.User
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface UserRepo : CursorBasedPaginationRepository<User, UUID> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
