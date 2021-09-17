@file:Suppress("Detekt.MatchingDeclarationName")

package com.diekeditora.authority.infra

import com.diekeditora.authority.domain.AuthorityService
import com.diekeditora.role.domain.Role
import com.diekeditora.user.domain.User
import com.diekeditora.utils.PaginationArg
import com.diekeditora.utils.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import graphql.relay.Connection
import org.springframework.stereotype.Component

@Component
class RoleAuthorityLoader(val authorityService: AuthorityService) :
    KotlinDataLoader<PaginationArg<Role, String>, Connection<String>> by dataLoader("RoleAuthoritiesLoader")
        .execute({ (role, first, after) ->
            authorityService.findAuthoritiesByRole(role, first, after)
        })

@Component
class UserAuthorityLoader(val authorityService: AuthorityService) :
    KotlinDataLoader<PaginationArg<User, String>, Connection<String>> by dataLoader("UserAuthoritiesLoader")
        .execute({ (user, first, after) ->
            authorityService.findAuthoritiesByUser(user, first, after)
        })

@Component
internal class AllUserAuthorityLoader(val authorityService: AuthorityService) :
    KotlinDataLoader<PaginationArg<User, String>, Connection<String>> by dataLoader("UserAllAuthoritiesLoader")
        .execute({ (user, first, after) ->
            authorityService.findAuthoritiesByUser(user, first, after)
        })
