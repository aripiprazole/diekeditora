package com.diekeditora.app.resources.authority

import com.diekeditora.domain.authority.Authority
import com.diekeditora.domain.authority.AuthorityService
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class AuthorityQuery(val authorityService: AuthorityService) : Query {
    @Secured(Authority.VIEW)
    suspend fun authorities(first: Int, after: String? = null): Connection<String> {
        return authorityService.findAllAuthorities(first, after)
    }
}
