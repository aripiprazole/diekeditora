package com.diekeditora.web.tests.graphql

import com.expediagroup.graphql.generator.exceptions.GraphQLKotlinException
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@Component
class GraphQLTestClient(val client: WebTestClient, val objectMapper: ObjectMapper) {
    final inline fun <R> request(
        query: TestQuery<R>,
        block: GraphQLRequestBuilder<R>.() -> Unit = {},
    ): R {
        val builder = GraphQLRequestBuilder(query).apply(block)

        val response = client
            .run {
                if (builder.authentication != null) {
                    mutateWith(mockAuthentication(builder.authentication!!))
                } else {
                    this
                }
            }
            .post().uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(builder.toRequest(objectMapper))
            .exchange()
            .expectBody<GraphQLResponse<Map<String, R>>>()
            .returnResult().responseBody!!

        response.errors?.let { errors ->
            throw GraphQLKotlinException(errors.joinToString(", "))
        }

        return response.data
            ?.get(query.queryName)
            ?: throw GraphQLKotlinException("GraphQLResponse.data is null")
    }
}

class GraphQLRequestBuilder<R>(query: TestQuery<R>) {
    var authentication: Authentication? = null
    var query: String? = query.content
    var operationName: String? = query.operationName
    var variables: Any? = null
}

fun GraphQLRequestBuilder<*>.toRequest(objectMapper: ObjectMapper): GraphQLRequest {
    val variablesMap = if (variables != null) {
        objectMapper.readValue<Map<String, Any>>(objectMapper.writeValueAsString(variables))
    } else {
        null
    }

    return GraphQLRequest(query!!, operationName!!, variablesMap)
}
