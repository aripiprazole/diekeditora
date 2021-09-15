package com.diekeditora.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.time.Instant
import java.util.Date

class DateSerializer : JsonSerializer<Date>() {
    override fun serialize(value: Date, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeNumber(value.toInstant().toEpochMilli())
    }
}

class DateDeserializer : JsonDeserializer<Date>() {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Date {
        return Date.from(Instant.ofEpochMilli(parser.readValueAs(jacksonTypeRef<Long>())))
    }
}
