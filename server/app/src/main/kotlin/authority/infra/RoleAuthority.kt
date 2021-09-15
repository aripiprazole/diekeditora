package com.diekeditora.authority.infra

import com.diekeditora.domain.id.UniqueId
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_role")
internal data class RoleAuthority(
    val userId: UniqueId,
    val authorityId: UniqueId,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
