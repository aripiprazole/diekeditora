package com.diekeditora.page.infra

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

@JsonSerialize(using = AppCursorSerializer::class)
@JsonDeserialize(using = AppCursorDeserializer::class)
data class AppCursor(private val value: String) : ConnectionCursor {
    override fun getValue(): String = value

    override fun toString(): String = value
}

internal class AppCursorDeserializer : JsonDeserializer<AppCursor>() {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): AppCursor {
        return AppCursor(parser.readValueAs(jacksonTypeRef<String>()))
    }
}

internal class AppCursorSerializer : JsonSerializer<AppCursor>() {
    override fun serialize(value: AppCursor, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.value)
    }
}
