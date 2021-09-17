package com.diekeditora.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(
        value: LocalDateTime,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        gen.writeString(ISO_LOCAL_DATE_TIME.format(value))
    }
}

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): LocalDateTime {
        return LocalDateTime.parse(
            parser.readValueAs(jacksonTypeRef<String>()),
            ISO_LOCAL_DATE_TIME
        )
    }
}
