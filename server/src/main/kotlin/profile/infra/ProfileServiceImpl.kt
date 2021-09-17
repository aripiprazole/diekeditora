package com.diekeditora.profile.infra

import com.diekeditora.id.domain.UniqueIdService
import com.diekeditora.profile.domain.Gender
import com.diekeditora.profile.domain.Profile
import com.diekeditora.profile.domain.ProfileService
import com.diekeditora.user.domain.User
import com.diekeditora.utils.logger
import org.springframework.stereotype.Service

@Service
internal class ProfileServiceImpl(
    val repo: ProfileRepo,
    val uidService: UniqueIdService,
) : ProfileService {
    private val log by logger()

    override suspend fun findOrCreateProfileByUser(user: User): Profile {
        val ownerId = requireNotNull(user.id) { "Owner id must be not null" }

        return repo.findByOwnerId(ownerId.toUUID())
            ?: repo.save(generateDefaultProfile(user))
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        requireNotNull(profile.id) { "Profile id must be not null when updating" }

        return repo.save(profile).also {
            log.trace("Successfully updated profile %s", profile)
        }
    }

    private fun generateDefaultProfile(user: User): Profile {
        return Profile(
            uid = uidService.generateUniqueId(),
            gender = Gender.NonBinary,
            ownerId = user.id!!,
        )
    }
}
