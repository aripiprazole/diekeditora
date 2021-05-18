package com.lorenzoog.diekeditora.web.tests.graphql

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient

@Component
class GraphQLTestClient(val client: WebTestClient, val json: Json)

inline fun <reified V, reified R : Any> GraphQLTestClient.request(
    query: TestQuery<V, R>,
    builder: GraphQLRequestBuilder<V, R>.() -> Unit = {}
): R {
    val string = client
        .post().uri("/graphql")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(
            json.encodeToString(GraphQLRequestBuilder(query).apply(builder).toRequest())
        )
        .exchange()
        .expectBody()
        .returnResult().responseBody!!
        .decodeToString()

    val response = json.decodeFromString<JsonObject>(string)
    val data = response["data"]?.jsonObject ?: run {
        val errors = response["errors"]!!.jsonArray

        throw GraphQLException(errors.map { it.jsonObject["message"]!!.jsonPrimitive.content })
    }

    return json.decodeFromJsonElement(data[query.queryName]!!)
}

class GraphQLException(errors: List<String>) : Exception(
    "Request failed with: ${errors.joinToString(", ")}"
)

class GraphQLRequestBuilder<V, R : Any>(testQuery: TestQuery<V, R>) {
    var query: String? = testQuery.query
    var operationName: String? = testQuery.operationName
    var variables: V? = null
}

@Serializable
data class GraphQLRequest<T>(
    val query: String,
    val operationName: String,
    val variables: T
)

fun <V> GraphQLRequestBuilder<V, *>.toRequest(): GraphQLRequest<V> {
    return GraphQLRequest(query!!, operationName!!, variables!!)
}
