package com.diekeditora.infra.serializers.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

class LocalDateSerializer : JsonSerializer<LocalDate>() {
    override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(ISO_LOCAL_DATE.format(value))
    }
}

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): LocalDate {
        return LocalDate.parse(parser.readValueAs(jacksonTypeRef<String>()), ISO_LOCAL_DATE)
    }
}
