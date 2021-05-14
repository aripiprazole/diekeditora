package com.lorenzoog.diekeditora.web.tests.utils

import com.expediagroup.graphql.server.types.GraphQLServerError
import com.lorenzoog.diekeditora.web.tests.graphql.TestQuery
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.springframework.test.web.reactive.server.WebTestClient

data class GraphQLException(val errors: List<GraphQLServerError>) : Exception() {
    override val message = "Could not execute the provided query due to: " +
        errors.joinToString(", ") { it.message }
}

@Suppress("MemberVisibilityCanBePrivate")
class GraphQLRequestBuilder<V, R : Any>(testQuery: TestQuery<V, R>) {
    var query: String? = testQuery.query
    var operationName: String? = testQuery.operationName
    var variables: V? = null
}

@Serializable
data class GraphQLRequest<T>(
    val query: String,
    val operationName: String,
    val variables: T?
)

fun <V, R : Any> GraphQLRequestBuilder<V, R>.toRequest(): GraphQLRequest<V> {
    return GraphQLRequest(query!!, operationName!!, variables!!)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <V, reified R : Any> WebTestClient.graphQL(
    json: Json,
    query: TestQuery<V, R>,
    builder: GraphQLRequestBuilder<V, R>.() -> Unit = {}
): R {
    val string = post().uri("/graphql")
        .bodyValue(GraphQLRequestBuilder(query).apply(builder).toRequest())
        .exchange()
        .expectBody()
        .returnResult().responseBody!!
        .decodeToString()

    val response = json.parseToJsonElement(string)
    val data = response.jsonObject["data"]!!.jsonObject[query.queryName]!!

    return json.decodeFromJsonElement(data)
}
