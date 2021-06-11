package com.diekeditora.domain.authority

interface AuthorityService {
    suspend fun findAllAuthorities(): Set<String>
}
