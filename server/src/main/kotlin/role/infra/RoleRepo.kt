package com.diekeditora.role.infra

import com.diekeditora.repo.domain.CursorBasedPaginationRepository
import com.diekeditora.role.domain.Role
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoleRepo : CursorBasedPaginationRepository<Role, UUID> {
    suspend fun findByName(name: String): Role?
}
