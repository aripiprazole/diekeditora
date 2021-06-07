package com.diekeditora.web.config

import com.diekeditora.infra.serializers.AnySerializersModule
import com.diekeditora.infra.serializers.GraphQLSerializersModule
import com.diekeditora.infra.serializers.JavaSerializersModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SerializerConfig {
    @Bean
    fun json(): Json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
        serializersModule = GraphQLSerializersModule + JavaSerializersModule + AnySerializersModule
    }
}
