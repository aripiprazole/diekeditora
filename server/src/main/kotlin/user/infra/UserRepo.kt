package com.diekeditora.user.infra

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.repo.domain.CursorBasedPaginationRepository
import com.diekeditora.user.domain.User
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepo : CursorBasedPaginationRepository<User, UniqueId> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
