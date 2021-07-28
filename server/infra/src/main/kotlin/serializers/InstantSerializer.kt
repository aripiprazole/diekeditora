package com.diekeditora.infra.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.time.Instant

class InstantSerializer : JsonSerializer<Instant>() {
    override fun serialize(value: Instant, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeNumber(value.toEpochMilli())
    }
}

class InstantDeserializer : JsonDeserializer<Instant>() {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Instant {
        return Instant.ofEpochMilli(parser.readValueAs(jacksonTypeRef<Long>()))
    }
}
