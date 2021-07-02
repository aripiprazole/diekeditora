package com.diekeditora.domain.user

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.role.Role
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDate
import java.time.LocalDateTime

@Table("\"user\"")
data class User(
    @Id
    @GraphQLIgnore
    @JsonIgnore
    val id: UniqueId? = null,
    val name: String,
    val email: String,
    val username: String,
    val birthday: LocalDate? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) : MutableEntity<User> {
    @GraphQLDescription("Finds the user's profile")
    suspend fun profile(env: DataFetchingEnvironment): Profile {
        return env
            .getDataLoader<User, Profile>("UserProfileLoader")
            .load(this)
            .await()
    }

    @PreAuthorize("hasAuthority('role.view')")
    suspend fun roles(env: DataFetchingEnvironment): List<Role> {
        return env
            .getDataLoader<User, Set<Role>>("UserRoleLoader")
            .load(this)
            .await()
            .toList()
    }

    @PreAuthorize("hasAuthority('authority.view')")
    suspend fun allAuthorities(env: DataFetchingEnvironment): List<String> {
        return env
            .getDataLoader<User, Set<String>>("AllUserAuthorityLoader")
            .load(this)
            .await()
            .toList()
    }

    @PreAuthorize("hasAuthority('authority.view')")
    suspend fun authorities(env: DataFetchingEnvironment): List<String> {
        return env
            .getDataLoader<User, Set<String>>("UserAuthorityLoader")
            .load(this)
            .await()
            .toList()
    }

    override fun update(with: User): User {
        return copy(
            username = with.username,
            name = with.name,
            email = with.email,
            updatedAt = LocalDateTime.now()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (birthday != other.birthday) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + birthday.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(" +
            "name='$name', " +
            "email='$email', " +
            "username='$username', " +
            "birthday=$birthday, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            "deletedAt=$deletedAt" +
            ")"
    }
}
