package com.diekeditora.domain.authority

interface AuthorityService {
    suspend fun createAuthorities(vararg authorities: String): String

    suspend fun deleteAuthorities(vararg authorities: String)
}
