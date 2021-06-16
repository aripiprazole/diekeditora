@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.domain.serializers.graphql

import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject

class GraphQLResponseSerializer<T>(
    private val tSerializer: KSerializer<T>
) : KSerializer<GraphQLResponse<T>> {
    @Serializable
    data class Surrogate<T>(
        val data: T?,
    )

    override val descriptor = Surrogate.serializer(tSerializer).descriptor

    override fun serialize(encoder: Encoder, value: GraphQLResponse<T>) {
        val surrogate = Surrogate(value.data)

        encoder.encodeSerializableValue(Surrogate.serializer(tSerializer), surrogate)
    }

    override fun deserialize(decoder: Decoder): GraphQLResponse<T> {
        val surrogate = decoder.decodeSerializableValue(Surrogate.serializer(tSerializer))

        return GraphQLResponse(surrogate.data)
    }
}

@Serializer(forClass = GraphQLRequest::class)
object GraphQLRequestSerializer {
    @Serializable
    data class Surrogate(
        val query: String,
        val operationName: String? = null,
        val variables: JsonObject? = null,
    )

    override val descriptor = Surrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: GraphQLRequest) {
        val surrogate = Surrogate(value.query, value.operationName)

        encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): GraphQLRequest {
        val surrogate = decoder.decodeSerializableValue(Surrogate.serializer())

        return GraphQLRequest(surrogate.query, surrogate.operationName)
    }
}
