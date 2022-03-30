package com.diekeditora.user.domain

import com.diekeditora.lib.UniqueId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class User(
  @Transient
  val id: UniqueId = UniqueId.New,
  val name: String,
  val email: String,
  val birthday: LocalDateTime,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)
