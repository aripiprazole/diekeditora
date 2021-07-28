package com.diekeditora.domain.profile

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.image.AvatarKind
import com.diekeditora.domain.image.FileKind
import com.diekeditora.domain.user.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("profile")
data class Profile(
    @Id
    @GraphQLIgnore
    val id: UniqueId? = null,
    val uid: UniqueId,
    val gender: Gender,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    @GraphQLIgnore val ownerId: UniqueId,
) : MutableEntity<Profile> {
    companion object Permissions {
        const val ADMIN = "profile.admin"
    }

    @GraphQLDescription("Returns profile's display name")
    suspend fun displayName(env: DataFetchingEnvironment): String {
        return user(env).username
    }

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
