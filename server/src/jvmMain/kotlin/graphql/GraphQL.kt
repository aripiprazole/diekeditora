@file:Suppress("Deprecation", "OverridingDeprecatedMember")

package com.diekeditora.graphql

import com.diekeditora.lib.ModelSchema
import com.expediagroup.graphql.generator.SchemaGenerator
import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import com.expediagroup.graphql.server.execution.GraphQLRequestParser
import com.expediagroup.graphql.server.types.GraphQLBatchRequest
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class DiekEditoraSchema : SchemaGenerator(SchemaGeneratorConfig(supportedPackages = listOf("kotlin"))) {
  init {
    addAdditionalTypesWithAnnotation(ModelSchema::class)
  }
}

class KtorGraphQLRequestParser(private val json: Json) : GraphQLRequestParser<ApplicationRequest> {
  override suspend fun parseRequest(request: ApplicationRequest): GraphQLServerRequest? {
    return when (val element = request.call.receive<JsonElement>()) {
      is JsonObject -> json.decodeFromJsonElement(GraphQLRequestSerializer, element)
      is JsonArray ->
        element
          .filterIsInstance<JsonObject>()
          .map { json.decodeFromJsonElement(GraphQLRequestSerializer, element) }
          .let { GraphQLBatchRequest(it) }
      else -> null
    }
  }
}

class KtorGraphQLContextFactory : GraphQLContextFactory<GraphQLContext, ApplicationRequest> {
  override suspend fun generateContext(request: ApplicationRequest): GraphQLContext? = null
}
