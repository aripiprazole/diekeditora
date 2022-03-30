package com.diekeditora.user.handlers

import com.diekeditora.lib.Handler
import com.diekeditora.user.infra.UserRepo

interface UserHandler<Req : UserRequest> : Handler<Req> {
  val userRepo: UserRepo
}

interface UserRequest
