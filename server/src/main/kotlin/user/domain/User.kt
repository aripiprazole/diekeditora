package com.diekeditora.user.domain

import com.diekeditora.MutableEntity
import com.diekeditora.Owned
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import com.diekeditora.page.domain.Cursor
import com.diekeditora.page.domain.OrderBy
import com.diekeditora.profile.domain.Profile
import com.diekeditora.role.domain.Role
import com.diekeditora.security.domain.Secured
import com.diekeditora.utils.PaginationArg
import com.diekeditora.utils.toPaginationArg
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
import org.springframework.security.access.prepost.PreAuthorize
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
) : MutableEntity<User>, Owned {
    @GraphQLDescription("Returns this user's profile details")
    suspend fun profile(env: DataFetchingEnvironment): Profile {
        return env
            .getDataLoader<User, Profile>("UserProfileLoader")
            .load(this)
            .await()
    }

    @Secured
    @GraphQLDescription("Returns favorite manga page")
    suspend fun favoriteMangas(
        env: DataFetchingEnvironment,
        first: Int,
        after: UniqueId? = null
    ): Connection<Manga> {
        return env
            .getDataLoader<PaginationArg<User, UniqueId>, Connection<Manga>>("UserFavoriteMangaLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    @Secured
    @GraphQLDescription("Returns last read manga page")
    suspend fun lastReadMangas(
        env: DataFetchingEnvironment,
        first: Int,
        after: UniqueId? = null
    ): Connection<Manga> {
        return env
            .getDataLoader<PaginationArg<User, UniqueId>, Connection<Manga>>("UserLastReadMangaLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    @Secured
    @PreAuthorize("hasAuthority('role.view') or authentication.principal.own(this)")
    @GraphQLDescription("Returns role page")
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

    @Secured
    @PreAuthorize("hasAuthority('authority.view') or authentication.principal.own(this)")
    @GraphQLDescription("Returns all authority page")
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

    @Secured
    @PreAuthorize("hasAuthority('authority.view') or authentication.principal.own(this)")
    @GraphQLDescription("Returns authority page")
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

    override val ownerId: UniqueId
        @GraphQLIgnore
        @JsonIgnore
        get() = id ?: error("Can not be owned without be fetched")

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
