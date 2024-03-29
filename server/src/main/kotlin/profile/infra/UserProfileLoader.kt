package com.diekeditora.profile.infra

import com.diekeditora.profile.domain.Profile
import com.diekeditora.profile.domain.ProfileService
import com.diekeditora.shared.infra.dataLoader
import com.diekeditora.user.domain.User
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
class UserProfileLoader(val profileService: ProfileService) :
    KotlinDataLoader<User, Profile> by dataLoader("UserProfileLoader")
        .execute({
            profileService.findOrCreateProfileByUser(it)
        })
