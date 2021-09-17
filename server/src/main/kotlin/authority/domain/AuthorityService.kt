package com.diekeditora.authority.domain

import com.diekeditora.role.domain.Role
import com.diekeditora.user.domain.User
import graphql.relay.Connection

interface AuthorityService {
    suspend fun findAllAuthorities(first: Int, after: String? = null): Connection<String>

    suspend fun findAllAuthoritiesByUser(user: User): Set<String>

    suspend fun findAuthoritiesByUser(
        user: User,
        first: Int,
        after: String? = null
    ): Connection<String>

    suspend fun findAuthoritiesByRole(
        role: Role,
        first: Int,
        after: String? = null
    ): Connection<String>

    suspend fun linkAuthorities(role: Role, authorities: Set<String>)

    suspend fun linkAuthorities(user: User, authorities: Set<String>)

    suspend fun unlinkAuthorities(role: Role, authorities: Set<String>)

    suspend fun unlinkAuthorities(user: User, authorities: Set<String>)
}
