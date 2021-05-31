package com.lorenzoog.diekeditora.domain.permission

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.serialization.SerialName
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("permission")
@SerialName("Permission")
data class Permission(
    @Transient
    @Id
    @GraphQLIgnore
    val id: UUID,
    val name: String
)
