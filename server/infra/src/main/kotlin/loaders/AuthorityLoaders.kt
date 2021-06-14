@file:Suppress("Detekt.MatchingDeclarationName")

package com.diekeditora.infra.loaders

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.authority.RoleService
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
class RoleAuthorityLoader(val roleService: RoleService) :
    KotlinDataLoader<Role, List<String>> by dataLoader("RoleAuthorityLoader").execute({ role ->
        roleService.findRoleAuthorities(role)
    })
