package com.diekeditora.web.resources.profile

import com.diekeditora.domain.image.AvatarKind
import com.diekeditora.domain.image.FileService
import com.diekeditora.domain.image.Upload
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.toByteArray
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component

@Component
class ProfileMutation(
    val fileService: FileService,
    val userService: UserService,
    val profileService: ProfileService,
) : Mutation {
    @GraphQLDescription("Updates username's profile avatar")
    @Secured(Profile.ADMIN)
    suspend fun uploadProfileAvatar(username: String, image: Upload) {
        val contentType = image.headers().contentType
            ?: error("Missing content type in image headers")

        val kind = userService.findUserByUsername(username)
            ?.let { user -> profileService.findOrCreateProfileByUser(user) }
            ?.let { profile -> AvatarKind(profile) }
            ?: return

        fileService.uploadFile(image.toByteArray(), kind, contentType)
    }
}
