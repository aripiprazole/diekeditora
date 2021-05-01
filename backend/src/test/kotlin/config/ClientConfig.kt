package com.lorenzoog.diekeditora.config

import kotlinx.serialization.json.Json
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class ClientConfig(val json: Json, val context: ApplicationContext) {
    @Bean
    fun webTestClient(): WebTestClient =
        WebTestClient.bindToApplicationContext(context).configureClient()
            .codecs {
                it.defaultCodecs().kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(json))
                it.defaultCodecs().kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(json))
            }
            .build()
}
