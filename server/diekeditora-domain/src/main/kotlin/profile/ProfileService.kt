package com.diekeditora.domain.profile

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.page.Page
import com.diekeditora.domain.user.User

interface ProfileService {
    suspend fun findProfiles(page: Int = 1): Page<Profile>

    suspend fun findOrCreateProfileByUser(user: User): Profile

    suspend fun updateProfile(profile: Profile): Profile

    suspend fun findProfileMangas(profile: Profile): List<Manga>
}