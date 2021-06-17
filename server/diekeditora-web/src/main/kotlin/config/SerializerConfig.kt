package com.diekeditora.web.config

import com.diekeditora.infra.serializers.time.DateDeserializer
import com.diekeditora.infra.serializers.time.DateSerializer
import com.diekeditora.infra.serializers.time.InstantDeserializer
import com.diekeditora.infra.serializers.time.InstantSerializer
import com.diekeditora.infra.serializers.time.LocalDateDeserializer
import com.diekeditora.infra.serializers.time.LocalDateSerializer
import com.diekeditora.infra.serializers.time.LocalDateTimeDeserializer
import com.diekeditora.infra.serializers.time.LocalDateTimeSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Configuration
class SerializerConfig {
    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()
        .registerModule(
            SimpleModule()
                .addSerializer(Instant::class.java, InstantSerializer())
                .addDeserializer(Instant::class.java, InstantDeserializer())
                .addSerializer(Date::class.java, DateSerializer())
                .addDeserializer(Date::class.java, DateDeserializer())
                .addSerializer(LocalDate::class.java, LocalDateSerializer())
                .addDeserializer(LocalDate::class.java, LocalDateDeserializer())
                .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        )
}
