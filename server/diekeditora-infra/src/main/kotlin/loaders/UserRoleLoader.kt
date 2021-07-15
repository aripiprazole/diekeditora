package com.diekeditora.infra.loaders

import com.diekeditora.domain.dataloader.PaginationArg
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import graphql.relay.Connection
import org.springframework.stereotype.Component

@Component
class UserRoleLoader(val roleService: RoleService) :
    KotlinDataLoader<PaginationArg<User, String>, Connection<Role>> by dataLoader("UserRolesLoader")
        .execute({ (user, first, after) ->
            roleService.findRolesByUser(user, first, after)
        })
