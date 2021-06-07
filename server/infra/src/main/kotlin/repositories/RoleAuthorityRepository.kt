package com.diekeditora.infra.repositories

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository

@Repository
interface RoleAuthorityRepository {
    suspend fun findByRole(role: Role): Flow<Authority>

    suspend fun save(role: Role, authority: Authority)

    suspend fun save(role: Role, authorities: Iterable<Authority>)
}
