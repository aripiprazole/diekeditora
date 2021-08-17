package com.diekeditora.domain.role

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.dataloader.PaginationArg
import com.diekeditora.domain.dataloader.toPaginationArg
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("role")
data class Role(
    @GraphQLIgnore
    @Id override val id: UniqueId? = null,
    @Cursor val name: String,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null
) : MutableEntity<Role> {

    @Secured
    @PreAuthorize("hasAuthority('authority.view')")
    @GraphQLDescription("Returns authority page")
    suspend fun authorities(
        env: DataFetchingEnvironment,
        first: Int,
        after: String? = null
    ): Connection<String> {
        return env
            .getDataLoader<PaginationArg<Role, String>, Connection<String>>("RoleAuthoritiesLoader")
            .load(toPaginationArg(first, after))
            .await()
    }

    @GraphQLIgnore
    override fun update(with: Role): Role {
        return copy(
            name = with.name,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (name != other.name) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        return name.hashCode()
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Role(" +
            "name='$name', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
