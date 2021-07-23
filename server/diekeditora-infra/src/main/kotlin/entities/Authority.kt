package com.diekeditora.infra.entities

import com.diekeditora.domain.Entity
import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("authority")
data class Authority(
    @Id
    @JsonIgnore
    @GraphQLIgnore
    val id: UniqueId? = null,
    val value: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : Entity<Authority> {
    @GraphQLIgnore
    override val cursor: String @JsonIgnore get() = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "Authority($value)"
    }

    companion object {
        fun of(value: String): Authority {
            return Authority(null, value)
        }
    }
}
