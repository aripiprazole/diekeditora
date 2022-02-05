package com.diekeditora.tests.utils

import org.springframework.test.web.reactive.server.MockServerConfigurer
import org.springframework.test.web.reactive.server.WebTestClient.MockServerSpec

fun MockServerSpec<*>.configure(configurer: MockServerConfigurer) =
    WebTestClientUtils.apply(this, configurer)
