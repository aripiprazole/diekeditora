package com.lorenzoog.diekeditora.domain.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class UserRequestDto(
    val name: String,
    val username: String,
    val email: String,
    val password: String? = null,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
    val emailVerifiedAt: @Contextual LocalDateTime? = null
)

@Serializable
data class UserResponseDto(
    val name: String,
    val username: String,
    val email: String,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime? = null,
    val updatedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
    val emailVerifiedAt: @Contextual LocalDateTime? = null
)
