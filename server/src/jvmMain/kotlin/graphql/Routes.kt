@file:Suppress("SameParameterValue", "Unchecked_Cast")
@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.graphql

import com.diekeditora.lib.NullSerializer
import com.diekeditora.lib.anyToJson
import com.expediagroup.graphql.generator.extensions.print
import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.execution.GraphQLServer
import com.expediagroup.graphql.server.types.GraphQLBatchResponse
import com.expediagroup.graphql.server.types.GraphQLResponse
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer

fun Route.graphqlRoutes(json: Json, schema: GraphQLSchema) {
  val graphql = GraphQL.newGraphQL(schema).build()

  val server = GraphQLServer(
    requestParser = KtorGraphQLRequestParser(json),
    contextFactory = KtorGraphQLContextFactory(),
    requestHandler = GraphQLRequestHandler(graphql),
  )

  post("graphql") {
    val element = when (val response = server.execute(call.request)) {
      is GraphQLResponse<*> -> encodeGraphQLResponse(json, response)
      is GraphQLBatchResponse -> response.responses.map { encodeGraphQLResponse(json, it) }.anyToJson()
      null -> return@post call.respond(HttpStatusCode.BadRequest, "Invalid request")
    }

    call.respond(element)
  }

  get("sdl") {
    call.respondText(schema.print())
  }

  get("playground") {
    call.respondText(buildPlaygroundHtml("graphql", "subscriptions"), ContentType.Text.Html)
  }
}

private fun encodeGraphQLResponse(json: Json, response: GraphQLResponse<*>): JsonElement {
  return when (response.data) {
    is Collection<*> -> {
      val serializer = GraphQLResponseSerializer(JsonArray.serializer())

      json.encodeToJsonElement(serializer, response.copy(data = (response.data as Collection<Any?>).anyToJson()))
    }
    is Map<*, *> -> {
      val serializer = GraphQLResponseSerializer(JsonObject.serializer())

      json.encodeToJsonElement(serializer, response.copy(data = (response.data as Map<Any, Any?>).anyToJson()))
    }
    null -> {
      json.encodeToJsonElement(GraphQLResponseSerializer(NullSerializer), response as GraphQLResponse<Any?>)
    }
    else -> {
      val serializer = GraphQLResponseSerializer(json.serializersModule.serializer(response.data!!.javaClass))

      json.encodeToJsonElement(serializer, response as GraphQLResponse<Any>)
    }
  }
}

private fun buildPlaygroundHtml(graphQLEndpoint: String, subscriptionsEndpoint: String): String {
  return Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
    ?.replace("\${graphQLEndpoint}", graphQLEndpoint)
    ?.replace("\${subscriptionsEndpoint}", subscriptionsEndpoint)
    ?: error("graphql-playground.html cannot be found in the classpath")
}
