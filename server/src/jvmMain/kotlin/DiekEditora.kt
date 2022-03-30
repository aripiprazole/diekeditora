package com.diekeditora

import com.diekeditora.graphql.DiekEditoraSchema
import com.diekeditora.graphql.graphqlRoutes
import com.diekeditora.user.infra.ExposedUserRepo
import com.diekeditora.user.resources.userRoutes
import com.expediagroup.graphql.generator.TopLevelObject
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import kotlinx.serialization.json.Json

private val queries = listOf<TopLevelObject>(TopLevelObject(HelloWorldQuery()))
private val mutations = listOf<TopLevelObject>()
private val subscriptions = listOf<TopLevelObject>()

class HelloWorldQuery {
  private val world = "world"

  fun hello(): String = world
}

fun main() {
  val schema = DiekEditoraSchema().use { generator ->
    generator.generateSchema(queries, mutations, subscriptions)
  }

  val userRepo = ExposedUserRepo()
  val json = Json

  embeddedServer(Jetty, port = 8080, host = "127.0.0.1") {
    install(ContentNegotiation) {
      json(json)
    }

    routing {
      graphqlRoutes(json, schema)
      userRoutes(userRepo)
    }
  }.start(wait = true)
}
