package com.diekeditora.domain.authority

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDateTime
import java.util.UUID

@SerialName("Role")
@Serializable
data class Role(
    @Id
    @GraphQLIgnore
    @kotlinx.serialization.Transient
    val id: UUID? = null,
    val name: String,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null
) {
    @PreAuthorize("hasAuthority('authority.view')")
    suspend fun authorities(env: DataFetchingEnvironment): List<String> {
        return env
            .getDataLoader<Role, List<String>>("RoleAuthorityLoader")
            .load(this)
            .await()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Role(" +
            "name='$name', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
