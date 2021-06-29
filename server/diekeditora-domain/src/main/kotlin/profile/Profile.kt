package com.diekeditora.domain.profile

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.user.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("profile")
data class Profile(
    @Id
    @GraphQLIgnore
    @JsonIgnore
    val id: UniqueId? = null,
    val gender: Gender,
    val user: User,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) {
    @GraphQLDescription("Returns profile's display name")
    val displayName: String
        get() = user.username
}
