package com.diekeditora.web.graphql.authority

import com.diekeditora.domain.authority.AuthorityService
import com.expediagroup.graphql.server.operations.Query
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class AuthorityQuery(val authorityService: AuthorityService) : Query {
    @PreAuthorize("hasAuthority('authority.view')")
    suspend fun authorities(): List<String> {
        return authorityService.findAllAuthorities().toList()
    }
}
