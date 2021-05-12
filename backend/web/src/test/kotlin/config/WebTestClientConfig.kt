package com.lorenzoog.diekeditora.web.config

import kotlinx.serialization.json.Json
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class WebTestClientConfig(val json: Json, val context: ApplicationContext) {
    @Bean
    fun client(): WebTestClient =
        WebTestClient.bindToApplicationContext(context).configureClient()
            .codecs {
                it.defaultCodecs()
                    .kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(json))

                it.defaultCodecs()
                    .kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(json))
            }
            .build()
}
