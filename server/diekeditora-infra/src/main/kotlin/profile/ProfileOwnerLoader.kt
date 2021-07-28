package com.diekeditora.infra.profile

import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
internal class ProfileOwnerLoader(val userService: UserService) :
    KotlinDataLoader<Profile, User> by dataLoader("ProfileOwnerLoader")
        .execute({
            userService.findUserById(it.ownerId)
                ?: error("The profile's ${it.uid} user should never be null")
        })
