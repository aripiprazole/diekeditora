package com.diekeditora.infra.role

import com.diekeditora.domain.id.UniqueId
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_role")
internal data class UserRole(
    val userId: UniqueId,
    val authorityId: UniqueId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)