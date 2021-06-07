package com.diekeditora.infra.repositories

import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository {
    suspend fun findAll(): Flow<Authority>

    suspend fun save(authority: Authority): Authority = saveAll(setOf(authority)).first()

    suspend fun saveAll(authorities: Iterable<Authority>): Set<Authority>

    suspend fun deleteAll(authorities: Iterable<Authority>)
}
