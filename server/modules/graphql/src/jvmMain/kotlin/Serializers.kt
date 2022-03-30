@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.graphql

import com.diekeditora.lib.anyToJson
import com.diekeditora.lib.jsonToAny
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLResponse
import com.expediagroup.graphql.server.types.GraphQLServerError
import com.expediagroup.graphql.server.types.GraphQLSourceLocation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

object GraphQLSourceLocationSerializer : KSerializer<GraphQLSourceLocation> {
  @Serializable
  private class Surrogate(val line: Int, val column: Int)

  override val descriptor: SerialDescriptor = Surrogate.serializer().descriptor

  override fun serialize(encoder: Encoder, value: GraphQLSourceLocation) {
    val surrogate = Surrogate(value.line, value.column)

    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
  }

  override fun deserialize(decoder: Decoder): GraphQLSourceLocation {
    val surrogate = decoder.decodeSerializableValue(Surrogate.serializer())

    return GraphQLSourceLocation(surrogate.line, surrogate.column)
  }
}

class GraphQLResponseSerializer<T>(private val tSerializer: KSerializer<T>) : KSerializer<GraphQLResponse<T>> {
  @Serializable
  private class Surrogate<T>(
    val data: T? = null,
    val errors: List<@Serializable(GraphQLServerErrorSerializer::class) GraphQLServerError>? = null,
    val extensions: JsonObject? = null,
  )

  override val descriptor: SerialDescriptor = Surrogate.serializer(tSerializer).descriptor

  override fun serialize(encoder: Encoder, value: GraphQLResponse<T>) {
    val surrogate = Surrogate(value.data, value.errors, value.extensions?.anyToJson())

    encoder.encodeSerializableValue(Surrogate.serializer(tSerializer), surrogate)
  }

  @Suppress("Unchecked_Cast")
  override fun deserialize(decoder: Decoder): GraphQLResponse<T> {
    val surrogate = decoder.decodeSerializableValue(Surrogate.serializer(tSerializer))

    return GraphQLResponse(
      data = surrogate.data,
      errors = surrogate.errors,
      extensions = surrogate.extensions?.jsonToAny() as Map<Any, Any?>?,
    )
  }
}

object GraphQLRequestSerializer : KSerializer<GraphQLRequest> {
  @Serializable
  private class Surrogate(val query: String, val operationName: String?, val variables: JsonObject? = null)

  override val descriptor: SerialDescriptor = Surrogate.serializer().descriptor

  override fun serialize(encoder: Encoder, value: GraphQLRequest) {
    val surrogate = Surrogate(value.query, value.operationName, value.variables?.anyToJson())

    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
  }

  override fun deserialize(decoder: Decoder): GraphQLRequest {
    val surrogate = decoder.decodeSerializableValue(Surrogate.serializer())

    return GraphQLRequest(surrogate.query, surrogate.operationName, surrogate.variables?.jsonToAny())
  }
}

object GraphQLServerErrorSerializer : KSerializer<GraphQLServerError> {
  @Serializable
  private class Surrogate(
    val message: String,
    val locations: List<@Serializable(GraphQLSourceLocationSerializer::class) GraphQLSourceLocation>? = null,
    val path: JsonArray? = null,
    val extensions: JsonObject? = null,
  )

  override val descriptor: SerialDescriptor = Surrogate.serializer().descriptor

  override fun serialize(encoder: Encoder, value: GraphQLServerError) {
    val surrogate = Surrogate(value.message, value.locations, value.path?.anyToJson(), value.extensions?.anyToJson())

    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
  }

  override fun deserialize(decoder: Decoder): GraphQLServerError {
    val surrogate = decoder.decodeSerializableValue(Surrogate.serializer())

    return GraphQLServerError(
      message = surrogate.message,
      locations = surrogate.locations,
      path = surrogate.path?.jsonToAny()?.filterNotNull(),
      extensions = surrogate.extensions?.jsonToAny(),
    )
  }
}
