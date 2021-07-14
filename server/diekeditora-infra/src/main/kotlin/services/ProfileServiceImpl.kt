package com.diekeditora.infra.services

import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.User
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
internal class ProfileServiceImpl : ProfileService {
    override suspend fun findProfiles(first: Int, after: String?): Connection<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun findOrCreateProfileByUser(user: User): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        TODO("Not yet implemented")
    }
}
