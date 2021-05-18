package com.lorenzoog.diekeditora.web.config

import com.lorenzoog.diekeditora.domain.config.AnySerializersModule
import com.lorenzoog.diekeditora.domain.config.GraphQLSerializersModule
import com.lorenzoog.diekeditora.domain.config.JavaSerializersModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
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
        serializersModule = GraphQLSerializersModule + JavaSerializersModule + AnySerializersModule
    }
}
