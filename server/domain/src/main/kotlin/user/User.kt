package com.diekeditora.domain.user

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.authority.Authority
import com.diekeditora.domain.dataloader.PaginationArg
import com.diekeditora.domain.dataloader.toPaginationArg
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.diekeditora.domain.profile.Profile
import com.diekeditora.domain.role.Role
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("\"user\"")
data class User(
    @GraphQLIgnore
    @Id override val id: UniqueId? = null,
    val name: String,
    val email: String,
    @Cursor val username: String,
    val birthday: LocalDate? = null,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) : MutableEntity<User> {
    companion object Permissions {
        const val VIEW = "user.view"
        const val STORE = "user.store"
        const val UPDATE = "user.update"
        const val DESTROY = "user.destroy"
    }

    @GraphQLDescription("Finds the user's profile")
    suspend fun profile(env: DataFetchingEnvironment): Profile {
        return env
            .getDataLoader<User, Profile>("UserProfileLoader")
            .load(this)
            .await()
    }

    @Secured(Role.VIEW)
    suspend fun roles(
        env: DataFetchingEnvironment,
        first: Int,
        after: String? = null
    ): Connection<Role> {
        return env
            .getDataLoader<PaginationArg<User, String>, Connection<Role>>("UserRolesLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    @Secured(Authority.VIEW)
    suspend fun allAuthorities(
        env: DataFetchingEnvironment,
        first: Int,
        after: String? = null,
    ): Connection<String> {
        return env
            .getDataLoader<PaginationArg<User, String>, Connection<String>>("UserAllAuthoritiesLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    @Secured(Authority.VIEW)
    suspend fun authorities(
        env: DataFetchingEnvironment,
        first: Int,
        after: String? = null,
    ): Connection<String> {
        return env
            .getDataLoader<PaginationArg<User, String>, Connection<String>>("UserAuthoritiesLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    override val cursor: String
        @GraphQLIgnore
        @JsonIgnore
        get() = username

    @GraphQLIgnore
    override fun update(with: User): User {
        return copy(
            username = with.username,
            name = with.name,
            email = with.email,
            updatedAt = LocalDateTime.now()
        )
    }

    @GraphQLIgnore
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

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + birthday.hashCode()
        return result
    }

    @GraphQLIgnore
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
