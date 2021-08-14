package com.diekeditora.infra.role

import com.diekeditora.domain.role.Role
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface RoleRepo : CursorBasedPaginationRepository<Role, UUID> {
    suspend fun findByName(name: String): Role?
}
