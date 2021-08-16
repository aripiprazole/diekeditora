package com.diekeditora.domain.profile

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.Owned
import com.diekeditora.domain.file.AvatarKind
import com.diekeditora.domain.file.FileKind
import com.diekeditora.domain.graphql.Authenticated
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.diekeditora.domain.user.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import com.fasterxml.jackson.annotation.JsonIgnore
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
    @Id override val id: UniqueId? = null,
    @Cursor val uid: UniqueId,
    val gender: Gender,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    @GraphQLIgnore override val ownerId: UniqueId,
) : MutableEntity<Profile>, Owned<User> {
    companion object Permissions {
        const val ADMIN = "profile.admin"
    }

    @GraphQLDescription("Returns profile's display name")
    suspend fun displayName(env: DataFetchingEnvironment): String {
        return user(env).username
    }

    @Secured
    @Authenticated
    @PreAuthorize("authentication.principal.own(this)")
    @GraphQLDescription("Returns profile's owner")
    suspend fun user(env: DataFetchingEnvironment): User {
        return env
            .getDataLoader<Profile, User>("ProfileOwnerLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns profile's avatar image url")
    suspend fun avatar(env: DataFetchingEnvironment): String {
        return env
            .getDataLoader<FileKind, String>("FileLinkLoader")
            .load(AvatarKind(this))
            .await()
    }

    @GraphQLIgnore
    override val cursor: String
        @JsonIgnore
        get() = uid.value

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
