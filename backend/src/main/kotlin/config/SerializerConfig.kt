package com.lorenzoog.diekeditora.config

import com.lorenzoog.diekeditora.serializers.InstantSerializer
import com.lorenzoog.diekeditora.serializers.LocalDateSerializer
import com.lorenzoog.diekeditora.serializers.LocalDateTimeSerializer
import com.lorenzoog.diekeditora.serializers.UUIDSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@AutoConfigureBefore(WebFluxAutoConfiguration::class)
class SerializerConfig {
    @Bean
    fun json(): Json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(LocalDateSerializer)
            contextual(LocalDateTimeSerializer)
            contextual(InstantSerializer)
            contextual(UUIDSerializer)
        }
    }
}
