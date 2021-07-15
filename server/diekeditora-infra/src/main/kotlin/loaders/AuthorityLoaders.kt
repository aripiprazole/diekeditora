@file:Suppress("Detekt.MatchingDeclarationName")

package com.diekeditora.infra.loaders

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.dataloader.PaginationArg
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.shared.dataLoader
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
class AllUserAuthorityLoader(val authorityService: AuthorityService) :
    KotlinDataLoader<PaginationArg<User, String>, Connection<String>> by dataLoader("UserAllAuthoritiesLoader")
        .execute({ (user, first, after) ->
            authorityService.findAuthoritiesByUser(user, first, after)
        })
