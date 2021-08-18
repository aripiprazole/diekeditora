package com.diekeditora.app.resources.profile

import com.diekeditora.domain.file.AvatarKind
import com.diekeditora.domain.file.FileService
import com.diekeditora.domain.file.Upload
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.profile.ProfileService
import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.toByteArray
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class ProfileMutation(
    val fileService: FileService,
    val userService: UserService,
    val profileService: ProfileService,
) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('profile.admin') or authentication.principal.username == #username")
    @GraphQLDescription("Updates username's profile avatar")
    suspend fun uploadProfileAvatar(username: String, image: Upload): Profile? {
        val kind = userService.findUserByUsername(username)
            ?.let { user -> profileService.findOrCreateProfileByUser(user) }
            ?.let { profile -> AvatarKind(profile) }
            ?: return null

        fileService.uploadFile(image.toByteArray(), kind, image.contentType)

        return kind.profile
    }
}
