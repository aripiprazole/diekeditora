package com.diekeditora.infra.services

import com.diekeditora.domain.id.UniqueIdService
import com.diekeditora.domain.profile.Gender
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.User
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
internal class ProfileServiceImpl(val uidService: UniqueIdService) : ProfileService {
    override suspend fun findProfiles(first: Int, after: String?): Connection<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun findOrCreateProfileByUser(user: User): Profile {
        return Profile(
            id = uidService.generateUniqueId(),
            gender = Gender.Female,
            user = user,
            avatarId = uidService.generateUniqueId()
        )
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        TODO("Not yet implemented")
    }
}
