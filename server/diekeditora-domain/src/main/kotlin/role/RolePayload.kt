package com.diekeditora.domain.role

import java.time.LocalDateTime

data class RolePayload(
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val authorities: List<String> = emptyList(),
)
