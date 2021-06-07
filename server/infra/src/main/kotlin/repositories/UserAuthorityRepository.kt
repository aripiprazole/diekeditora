package com.diekeditora.infra.repositories

import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository

@Repository
interface UserAuthorityRepository {
    suspend fun findByUser(user: User): Flow<Authority>

    suspend fun save(user: User, authority: Authority)

    suspend fun save(user: User, authorities: Iterable<Authority>)
}
