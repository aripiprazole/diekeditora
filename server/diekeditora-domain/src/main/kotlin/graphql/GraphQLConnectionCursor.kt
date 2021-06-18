package com.diekeditora.domain.graphql

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import graphql.relay.ConnectionCursor

@JsonSerialize(using = GraphQLConnectionCursorSerializer::class)
@JsonDeserialize(using = GraphQLConnectionCursorDeserializer::class)
class GraphQLConnectionCursor(private val value: String) : ConnectionCursor {
    override fun getValue() = value
}

internal class GraphQLConnectionCursorDeserializer : JsonDeserializer<GraphQLConnectionCursor>() {
    override fun deserialize(
        parser: JsonParser,
        ctx: DeserializationContext
    ): GraphQLConnectionCursor {
        return GraphQLConnectionCursor(parser.readValueAs(jacksonTypeRef<String>()))
    }
}

internal class GraphQLConnectionCursorSerializer : JsonSerializer<GraphQLConnectionCursor>() {
    override fun serialize(
        value: GraphQLConnectionCursor,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        gen.writeString(value.value)
    }
}
