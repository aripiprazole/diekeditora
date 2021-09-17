package com.diekeditora.newsletter.domain

import com.diekeditora.shared.domain.MutableEntity
import com.diekeditora.database.domain.NewsletterId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("newsletter")
data class Newsletter(
    @GraphQLIgnore
    @Id
    override val id: NewsletterId = NewsletterId.New,
    val email: String,
) : MutableEntity<Newsletter, NewsletterId> {
    @GraphQLIgnore
    override val cursor: String
        @JsonIgnore get() = email

    @GraphQLIgnore
    override fun update(with: Newsletter): Newsletter {
        return copy(email = with.email)
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Newsletter

        if (email != other.email) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        return email.hashCode()
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Newsletter(email='$email')"
    }
}
