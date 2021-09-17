package com.diekeditora.authority.infra

import com.diekeditora.repo.domain.CursorBasedPaginationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthorityRepo : CursorBasedPaginationRepository<Authority, UUID> {
    suspend fun findByValue(value: String): Authority?
}
