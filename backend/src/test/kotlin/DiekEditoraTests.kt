package com.lorenzoog.diekeditora

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
class DiekEditoraTests(@Autowired val json: Json, @Autowired val client: WebTestClient) {

    @Test
    fun contextLoads() {
        //
    }
}
