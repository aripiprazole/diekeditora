package com.diekeditora.user.handlers

import com.diekeditora.lib.Cursor
import com.diekeditora.lib.HandlerScope
import com.diekeditora.user.domain.User
import com.diekeditora.user.infra.UserRepo

class GetUserRequest(val first: Int, val after: String? = null) : UserRequest

class GetUserHandler(override val userRepo: UserRepo) : UserHandler<GetUserRequest> {
  override suspend fun HandlerScope.handle(request: GetUserRequest): Cursor<User> {
    guard("users.get")

    return userRepo.findUsers(request.first, request.after)
  }
}
