package com.diekeditora.authority.infra

import com.diekeditora.com.diekeditora.repo.CursorBasedPaginationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface AuthorityRepo : CursorBasedPaginationRepository<Authority, UUID> {
    suspend fun findByValue(value: String): Authority?
}
