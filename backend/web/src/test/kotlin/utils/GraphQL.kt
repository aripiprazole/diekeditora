package com.lorenzoog.diekeditora.web.utils

import com.expediagroup.graphql.server.types.GraphQLResponse
import com.lorenzoog.diekeditora.web.graphql.TestQuery
import kotlinx.serialization.Serializable
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@Serializable
data class GraphQLRequest<T>(
    val query: String,
    val operationName: String,
    val variables: T?
)

@Suppress("MemberVisibilityCanBePrivate")
class GraphQLRequestBuilder<V, R : Any>(testQuery: TestQuery<V, R>) {
    var query: String? = testQuery.query
    var operationName: String? = testQuery.operationName
    var variables: V? = null

    fun toRequest(): GraphQLRequest<V> {
        return GraphQLRequest(query!!, operationName!!, variables!!)
    }
}

fun <V, R : Any> WebTestClient.graphQL(
    query: TestQuery<V, R>,
    builder: GraphQLRequestBuilder<V, R>.() -> Unit = {}
): R {
    return post().uri("/graphql")
        .bodyValue(GraphQLRequestBuilder<V, R>(query).apply(builder).toRequest())
        .exchange()
        .expectBody<GraphQLResponse<R>>()
        .returnResult()
        .responseBody?.data!!
}
