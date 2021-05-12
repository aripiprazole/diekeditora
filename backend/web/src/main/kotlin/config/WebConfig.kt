package com.lorenzoog.diekeditora.web.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebConfig(val json: Json) : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs()
            .kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(json))

        configurer.defaultCodecs()
            .kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(json))
    }
}
