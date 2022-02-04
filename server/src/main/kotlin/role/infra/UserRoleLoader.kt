package com.diekeditora.role.infra

import com.diekeditora.role.domain.Role
import com.diekeditora.role.domain.RoleService
import com.diekeditora.shared.infra.PaginationArg
import com.diekeditora.shared.infra.dataLoader
import com.diekeditora.user.domain.User
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import graphql.relay.Connection
import org.springframework.stereotype.Component

@Component
class UserRoleLoader(val roleService: RoleService) :
    KotlinDataLoader<PaginationArg<User, String>, Connection<Role>> by dataLoader("UserRolesLoader")
        .execute({ (user, first, after) ->
            roleService.findRolesByUser(user, first, after)
        })
