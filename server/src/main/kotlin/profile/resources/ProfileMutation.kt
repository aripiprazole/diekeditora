package com.diekeditora.profile.resources

import com.diekeditora.file.domain.AvatarKind
import com.diekeditora.file.domain.FileService
import com.diekeditora.file.domain.Upload
import com.diekeditora.profile.domain.Profile
import com.diekeditora.profile.domain.ProfileService
import com.diekeditora.security.domain.Secured
import com.diekeditora.shared.infra.toByteArray
import com.diekeditora.user.domain.UserService
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
