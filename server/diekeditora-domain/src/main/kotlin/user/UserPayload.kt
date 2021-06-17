package com.diekeditora.domain.user

import com.diekeditora.domain.role.Role
import java.time.LocalDate
import java.time.LocalDateTime

data class UserPayload(
    val name: String,
    val username: String,
    val email: String,
    val birthday: LocalDate? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
    val authorities: List<String> = emptyList(),
    val roles: List<Role> = emptyList(),
)
