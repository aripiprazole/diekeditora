package com.diekeditora.newsletter.domain

import com.diekeditora.MutableEntity
import com.diekeditora.id.domain.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("newsletter")
data class Newsletter(
    @GraphQLIgnore
    @Id
    override val id: UniqueId? = null,
    val email: String,
    val active: Boolean = true,
) : MutableEntity<Newsletter> {
    @GraphQLIgnore
    override val cursor: String
        @JsonIgnore get() = email

    @GraphQLIgnore
    override fun update(with: Newsletter): Newsletter {
        return copy(email = with.email, active = with.active)
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Newsletter

        if (email != other.email) return false
        if (active != other.active) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + active.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Newsletter(" +
            "email='$email', " +
            "active=$active" +
            ")"
    }
}
