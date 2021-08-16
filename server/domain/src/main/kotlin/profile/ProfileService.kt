package com.diekeditora.domain.profile

import com.diekeditora.domain.user.User

interface ProfileService {
    suspend fun findOrCreateProfileByUser(user: User): Profile

    suspend fun updateProfile(profile: Profile): Profile
}
