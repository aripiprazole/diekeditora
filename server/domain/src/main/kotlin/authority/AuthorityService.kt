package com.diekeditora.domain.authority

interface AuthorityService {
    suspend fun findAllAuthorities(): Set<String>

    suspend fun createAuthorities(vararg authorities: String): Set<String>

    suspend fun deleteAuthorities(vararg authorities: String)
}
