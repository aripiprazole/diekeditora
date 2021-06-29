package com.diekeditora.domain.manga

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("chapter")
data class Chapter(
    @Id
    @GraphQLIgnore
    @JsonIgnore
    val id: UUID? = null,
    val title: String,
    val pages: Int,
    val enabled: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
)
