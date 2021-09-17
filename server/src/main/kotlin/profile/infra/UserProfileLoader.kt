package com.diekeditora.profile.infra

import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.User
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
internal class UserProfileLoader(val profileService: ProfileService) :
    KotlinDataLoader<User, Profile> by dataLoader("UserProfileLoader")
        .execute({
            profileService.findOrCreateProfileByUser(it)
        })
