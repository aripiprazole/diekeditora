package com.diekeditora.user.infra

import com.diekeditora.lib.Cursor
import com.diekeditora.user.domain.User
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExposedUserRepo : UserRepo {
  fun nowLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
  }

  override suspend fun findUsers(first: Int, after: String?): Cursor<User> {
    return Cursor(
      listOf(
        User(
          name = "José Carlos",
          email = "josecarlos0143@gmail.com",
          birthday = nowLocalDateTime(),
          createdAt = nowLocalDateTime(),
          updatedAt = nowLocalDateTime(),
        )
      )
    )
  }
}
