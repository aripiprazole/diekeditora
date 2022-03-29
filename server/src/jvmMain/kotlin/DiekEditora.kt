package com.diekeditora

import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

fun main() {
  embeddedServer(Jetty, port = 8080, host = "127.0.0.1") {
    routing {
    }
  }.start(wait = true)
}
