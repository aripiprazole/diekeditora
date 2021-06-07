package com.diekeditora.domain.authority

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@SerialName("Role")
@Serializable
data class Role(
    @GraphQLIgnore
    @kotlinx.serialization.Transient
    val id: UUID? = null,
    val name: String,
    val authorities: Set<String>,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (name != other.name) return false
        if (authorities != other.authorities) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + authorities.hashCode()
        return result
    }

    override fun toString(): String {
        return "Role(" +
            "name='$name', " +
            "authorities=$authorities, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
