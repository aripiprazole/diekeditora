package com.diekeditora.domain.connection

import com.diekeditora.domain.user.User

interface SocialService {
    suspend fun findAllSocialsByUser(user: User): Set<Social>

    suspend fun addSocial(user: User, social: Social)
}
