package com.diekeditora.infra.profile

import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.infra.redis.CacheProvider
import com.diekeditora.infra.redis.expiresIn
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Primary
@Service
@OptIn(ExperimentalTime::class)
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
