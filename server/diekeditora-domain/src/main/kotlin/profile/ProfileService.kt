package com.diekeditora.domain.profile

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.user.User
import graphql.relay.Connection

interface ProfileService {
    suspend fun findProfiles(first: Int, after: String? = null): Connection<Profile>

    suspend fun findOrCreateProfileByUser(user: User): Profile

    suspend fun updateProfile(profile: Profile): Profile

    suspend fun findProfileMangas(profile: Profile): List<Manga>
}
