package com.diekeditora.infra.services

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.page.Page
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.User
import org.springframework.stereotype.Service

@Service
internal class ProfileServiceImpl : ProfileService {
    override suspend fun findProfiles(page: Int): Page<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun findOrCreateProfileByUser(user: User): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun findProfileMangas(profile: Profile): List<Manga> {
        TODO("Not yet implemented")
    }
}
