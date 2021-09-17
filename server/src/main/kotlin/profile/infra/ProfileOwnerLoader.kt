package com.diekeditora.profile.infra

import com.diekeditora.profile.domain.Profile
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserService
import com.diekeditora.shared.infra.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
class ProfileOwnerLoader(val userService: UserService) :
    KotlinDataLoader<Profile, User> by dataLoader("ProfileOwnerLoader")
        .execute({
            userService.findUserById(it.ownerId)
                ?: error("The profile's ${it.uid} user should never be null")
        })
