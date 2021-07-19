package com.diekeditora.infra.entities

import com.diekeditora.domain.id.UniqueId
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_role")
data class UserRole(
    val userId: UniqueId,
    val authorityId: UniqueId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
