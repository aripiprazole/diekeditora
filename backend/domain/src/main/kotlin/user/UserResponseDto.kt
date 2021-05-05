package com.lorenzoog.diekeditora.domain.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class UserResponseDto(
    val name: String,
    val username: String,
    val email: String,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime,
    val updatedAt: @Contextual LocalDateTime?,
    val deletedAt: @Contextual LocalDateTime?,
    val emailVerifiedAt: @Contextual LocalDateTime?
)
