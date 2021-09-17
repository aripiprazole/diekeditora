package com.diekeditora.profile.domain

import com.diekeditora.user.domain.User

interface ProfileService {
    suspend fun findOrCreateProfileByUser(user: User): Profile

    suspend fun updateProfile(profile: Profile): Profile
}
