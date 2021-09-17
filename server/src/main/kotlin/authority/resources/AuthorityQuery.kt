package com.diekeditora.app.authority.resource

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.graphql.Secured
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class AuthorityQuery(val authorityService: AuthorityService) : Query {
    @Secured
    @PreAuthorize("hasAuthority('authority.view')")
    @GraphQLDescription("Returns authority page")
    suspend fun authorities(first: Int, after: String? = null): Connection<String> {
        return authorityService.findAllAuthorities(first, after)
    }
}
