package com.diekeditora

import com.diekeditora.user.infra.ExposedUserRepo
import com.diekeditora.user.resources.userRoutes
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

fun main() {
  val userRepo = ExposedUserRepo()

  embeddedServer(Jetty, port = 8080, host = "127.0.0.1") {
    install(ContentNegotiation) {
      json()
    }

    routing {
      userRoutes(userRepo)
    }
  }.start(wait = true)
}
