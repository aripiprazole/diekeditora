package com.diekeditora.role.infra

import com.diekeditora.com.diekeditora.repo.CursorBasedPaginationRepository
import com.diekeditora.role.domain.Role
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface RoleRepo : CursorBasedPaginationRepository<Role, UUID> {
    suspend fun findByName(name: String): Role?
}
