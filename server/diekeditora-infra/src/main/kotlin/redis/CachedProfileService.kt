package com.diekeditora.infra.redis

import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
internal class CachedProfileService(
    val delegate: ProfileService,
    val cacheProvider: CacheProvider,
) : ProfileService by delegate {
    override suspend fun findProfiles(first: Int, after: String?): Connection<Profile> {
        return cacheProvider
            .repo<Connection<Profile>>()
            .remember("profileConnection.$first.$after", expiresIn) {
                delegate.findProfiles(first, after)
            }
    }
}
