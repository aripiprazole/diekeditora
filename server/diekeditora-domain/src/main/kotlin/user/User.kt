package com.diekeditora.domain.user

import com.diekeditora.domain.role.Role
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Table("\"user\"")
data class User(
    @Id
    @GraphQLIgnore
    @JsonIgnore
    val id: UUID? = null,
    val name: String,
    val email: String,
    val username: String,
    val birthday: LocalDate? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    @PreAuthorize("hasAuthority('role.view')")
    suspend fun roles(env: DataFetchingEnvironment): List<Role> {
        return env
            .getDataLoader<User, List<Role>>("UserRoleLoader")
            .load(this)
            .await()
    }

    @PreAuthorize("hasAuthority('authority.view')")
    suspend fun allAuthorities(env: DataFetchingEnvironment): List<String> {
        return env
            .getDataLoader<User, List<String>>("AllUserAuthorityLoader")
            .load(this)
            .await()
    }

    @PreAuthorize("hasAuthority('authority.view')")
    suspend fun authorities(env: DataFetchingEnvironment): List<String> {
        return env
            .getDataLoader<User, List<String>>("UserAuthorityLoader")
            .load(this)
            .await()
    }

    @GraphQLIgnore
    fun update(user: User): User {
        return copy(
            username = user.username,
            name = user.name,
            email = user.email,
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
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (deletedAt?.hashCode() ?: 0)
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
