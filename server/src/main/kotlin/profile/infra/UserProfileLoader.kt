package com.diekeditora.profile.infra

import com.diekeditora.profile.domain.Profile
import com.diekeditora.profile.domain.ProfileService
import com.diekeditora.user.domain.User
import com.diekeditora.utils.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
internal class UserProfileLoader(val profileService: ProfileService) :
    KotlinDataLoader<User, Profile> by dataLoader("UserProfileLoader")
        .execute({
            profileService.findOrCreateProfileByUser(it)
        })
