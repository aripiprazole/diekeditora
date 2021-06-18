package com.diekeditora.domain.authority

import com.diekeditora.domain.user.User

interface AuthorityService {
    suspend fun findAllAuthorities(): Set<String>

    suspend fun findALlAuthoritiesByUser(user: User): Set<String>
}
