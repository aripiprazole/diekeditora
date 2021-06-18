@file:Suppress("Detekt.MatchingDeclarationName")

package com.diekeditora.infra.loaders

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
class RoleAuthorityLoader(val roleService: RoleService) :
    KotlinDataLoader<Role, List<String>> by dataLoader("RoleAuthorityLoader").execute({ role ->
        roleService.findRoleAuthorities(role)
    })

@Component
class UserAuthorityLoader(val userService: UserService) :
    KotlinDataLoader<User, List<String>> by dataLoader("UserAuthorityLoader").execute({ user ->
        userService.findUserAuthorities(user)
    })

@Component
class AllUserAuthorityLoader(val authorityService: AuthorityService) :
    KotlinDataLoader<User, List<String>> by dataLoader("AllUserAuthorityLoader").execute({ user ->
        authorityService.findALlAuthoritiesByUser(user).toList()
    })
