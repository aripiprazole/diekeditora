package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("genre")
data class Genre(
    val id: UniqueId? = null,
    val title: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
)
