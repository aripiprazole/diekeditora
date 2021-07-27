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
    val gender: Gender,
    val user: User,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    @GraphQLIgnore
    val avatarId: UniqueId,
) : MutableEntity<Profile> {
    @GraphQLDescription("Returns profile's display name")
    val displayName: String
        get() = user.username

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
        get() = displayName

    @GraphQLIgnore
    override fun update(with: Profile): Profile {
        return copy(
            gender = with.gender,
            user = with.user,
            updatedAt = LocalDateTime.now()
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Profile

        if (gender != other.gender) return false
        if (user != other.user) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = gender.hashCode()
        result = 31 * result + user.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Profile(" +
            "gender=$gender, " +
            "user=$user, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            "displayName='$displayName'" +
            ")"
    }
}
