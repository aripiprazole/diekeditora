package com.diekeditora.infra.user

import com.diekeditora.domain.user.User
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface UserRepo : CursorBasedPaginationRepository<User, UUID> {
    @Query("""update "user" set deleted_at = current_database() where username = :username""")
    override suspend fun delete(entity: User)

    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
