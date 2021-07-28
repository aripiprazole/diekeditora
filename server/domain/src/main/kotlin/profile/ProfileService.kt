package com.diekeditora.domain.profile

import com.diekeditora.domain.user.User
import graphql.relay.Connection

interface ProfileService {
    suspend fun findOrCreateProfileByUser(user: User): Profile

    suspend fun updateProfile(profile: Profile): Profile
}
