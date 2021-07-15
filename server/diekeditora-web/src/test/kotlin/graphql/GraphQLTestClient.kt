package com.diekeditora.web.tests.graphql

import com.diekeditora.web.tests.utils.AuthenticationMocker
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
import kotlin.reflect.full.declaredMemberProperties

interface GraphQLRequestBuilder<R> {
    fun authenticate(vararg authorities: String)
}

abstract class TestQuery<R>(val content: String) {
    val variables: Map<String, Any?>
        get() = this::class.declaredMemberProperties
            .associate { property ->
                property.name to property.getter.call(this)
            }
}

@Component
class GraphQLTestClient(
    val authenticationMocker: AuthenticationMocker,
    val client: WebTestClient,
    val objectMapper: ObjectMapper,
) {
    final inline fun <reified R> request(
        query: TestQuery<R>,
        block: GraphQLRequestBuilder<R>.() -> Unit = {}
    ): R {
        val builder = GraphQLRequestBuilderImpl(authenticationMocker, query).apply(block)

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
            .bodyValue(builder.toRequest())
            .exchange()
            .expectBody<GraphQLResponse<Map<String, Any>>>()
            .returnResult().responseBody!!

        response.errors?.let { errors ->
            throw GraphQLKotlinException(errors.joinToString(", ") { it.message })
        }

        return response.data
            ?.entries
            ?.toList()?.get(0)
            ?.let(objectMapper::writeValueAsString)
            ?.let { objectMapper.readValue<R>(it) }
            ?: throw GraphQLKotlinException("GraphQLResponse.data is null")
    }
}

class GraphQLRequestBuilderImpl<R>(
    val authenticationMocker: AuthenticationMocker,
    val query: TestQuery<R>
) : GraphQLRequestBuilder<R> {
    var authentication: Authentication? = null

    override fun authenticate(vararg authorities: String) {
        authentication = authenticationMocker.mock(authorities = authorities)
    }

    fun toRequest(): GraphQLRequest {
        return GraphQLRequest(query.content, variables = query.variables)
    }
}
