package com.diekeditora.user.infra

import com.diekeditora.lib.Cursor
import com.diekeditora.user.domain.User

interface UserRepo {
  suspend fun findUsers(first: Int, after: String? = null): Cursor<User>
}
