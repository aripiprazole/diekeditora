package com.diekeditora.web.tests.config

import com.diekeditora.web.tests.utils.configure
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class WebTestClientConfig(val objectMapper: ObjectMapper, val context: ApplicationContext) {
    @Bean
    fun client(): WebTestClient =
        WebTestClient
            .bindToApplicationContext(context)
            .configure(springSecurity())
            .configureClient()
            .codecs { configurer ->
                configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
            }
            .build()
}
