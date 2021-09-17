package com.diekeditora.user.infra

import com.diekeditora.domain.user.User
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface UserRepo : CursorBasedPaginationRepository<User, UUID> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
