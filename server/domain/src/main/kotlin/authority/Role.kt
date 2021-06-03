package com.diekeditora.domain.authority

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.serialization.SerialName
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("role")
@SerialName("Role")
data class Role(
    @Transient
    @Id
    @GraphQLIgnore
    val id: UUID,
    val name: String,
    val authorities: List<String>
)
