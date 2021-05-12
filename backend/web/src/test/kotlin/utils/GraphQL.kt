package com.lorenzoog.diekeditora.web.utils

import com.expediagroup.graphql.server.types.GraphQLRequest
import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.request(query: String): WebTestClient.RequestHeadersSpec<*> {
    return post()
        .uri("/graphql")
        .bodyValue(GraphQLRequest(query))
}
