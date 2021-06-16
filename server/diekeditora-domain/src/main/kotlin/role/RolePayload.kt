package com.diekeditora.domain.role

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class RolePayload(
    val name: String,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null,
    val authorities: List<String> = emptyList(),
)
