package com.diekeditora.profile.infra

import com.diekeditora.domain.id.UniqueIdService
import com.diekeditora.domain.profile.Gender
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.User
import com.diekeditora.shared.logger
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
