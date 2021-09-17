package com.diekeditora.profile.domain

import com.diekeditora.shared.domain.MutableEntity
import com.diekeditora.shared.domain.BelongsTo
import com.diekeditora.file.domain.AvatarKind
import com.diekeditora.file.domain.FileKind
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.page.domain.OrderBy
import com.diekeditora.security.domain.Authenticated
import com.diekeditora.security.domain.Secured
import com.diekeditora.shared.refs.ProfileId
import com.diekeditora.shared.refs.UserId
import com.diekeditora.user.domain.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("profile")
data class Profile(
    @GraphQLIgnore
    @Id override val id: ProfileId = ProfileId.New,
    @Cursor val uid: UniqueId,
    val gender: Gender,
    val bio: String = "",
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    @GraphQLIgnore override val ownerId: UserId,
) : MutableEntity<Profile, ProfileId>, BelongsTo<UserId> {
    @GraphQLDescription("Returns profile's display name")
    suspend fun displayName(env: DataFetchingEnvironment): String {
        return user(env).username
    }

    @GraphQLDescription("Returns profile's avatar image url")
    suspend fun avatar(env: DataFetchingEnvironment): String {
        return env
            .getDataLoader<FileKind, String>("FileLinkLoader")
            .load(AvatarKind(this))
            .await()
    }

    @Secured
    @Authenticated
    @PreAuthorize("hasAuthority('user.view') or authentication.principal.own(this)")
    @GraphQLDescription("Returns profile's owner")
    suspend fun user(env: DataFetchingEnvironment): User {
        return env
            .getDataLoader<Profile, User>("ProfileOwnerLoader")
            .load(this)
            .await()
    }

    @GraphQLIgnore
    override fun update(with: Profile): Profile {
        return copy(gender = with.gender, updatedAt = LocalDateTime.now())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Profile

        if (uid != other.uid) return false

        return true
    }

    override fun hashCode(): Int {
        return uid.hashCode()
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Profile(" +
            "uid=$uid, " +
            "gender=$gender, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            ")"
    }
}
