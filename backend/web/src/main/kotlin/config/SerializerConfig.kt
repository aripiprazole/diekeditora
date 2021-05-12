package com.lorenzoog.diekeditora.web.config

import com.lorenzoog.diekeditora.web.serializers.InstantSerializer
import com.lorenzoog.diekeditora.web.serializers.LocalDateSerializer
import com.lorenzoog.diekeditora.web.serializers.LocalDateTimeSerializer
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
        }
    }
}
