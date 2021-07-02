package com.diekeditora.infra.loaders

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
class UserRoleLoader(val userService: UserService) :
    KotlinDataLoader<User, Set<Role>> by dataLoader("UserRoleLoader").execute({ user ->
        userService.findUserRoles(user)
    })
