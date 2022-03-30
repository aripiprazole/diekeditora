package com.diekeditora.user.resources

import com.diekeditora.lib.Handler
import com.diekeditora.lib.fix
import com.diekeditora.lib.handle
import com.diekeditora.user.handlers.GetUserHandler
import com.diekeditora.user.handlers.GetUserRequest
import com.diekeditora.user.handlers.UserRequest
import com.diekeditora.user.infra.UserRepo
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.getValue

fun Route.userRoutes(userRepo: UserRepo) {
  getUsersRoute(GetUserHandler(userRepo).fix())
}

fun Route.getUsersRoute(handler: Handler<UserRequest>) {
  get("/users") {
    val first: Int by call.parameters

    handle(handler, GetUserRequest(first, call.parameters["after"]))
  }
}
