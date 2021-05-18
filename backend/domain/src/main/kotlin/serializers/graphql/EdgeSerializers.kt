package com.lorenzoog.diekeditora.domain.serializers.graphql

import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultEdge
import graphql.relay.Edge
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
@SerialName("Edge")
data class EdgeSurrogate<T>(val node: @Contextual T, val cursor: String)

class EdgeSerializer<T>(private val tSerializer: KSerializer<T>) : KSerializer<Edge<T>> {
    override val descriptor = EdgeSurrogate.serializer(tSerializer).descriptor

    override fun serialize(encoder: Encoder, value: Edge<T>) {
        encoder.encodeEdge(value, tSerializer)
    }

    override fun deserialize(decoder: Decoder): Edge<T> {
        return decoder.decodeEdge(tSerializer)
    }
}

class DefaultEdgeSerializer<T>(
    private val tSerializer: KSerializer<T>
) : KSerializer<DefaultEdge<T>> {
    override val descriptor = EdgeSurrogate.serializer(tSerializer).descriptor

    override fun serialize(encoder: Encoder, value: DefaultEdge<T>) {
        encoder.encodeEdge(value, tSerializer)
    }

    override fun deserialize(decoder: Decoder): DefaultEdge<T> {
        return decoder.decodeEdge(tSerializer)
    }
}

private fun <T> Encoder.encodeEdge(value: Edge<T>, tSerializer: KSerializer<T>) {
    val surrogate = EdgeSurrogate(value.node, value.cursor.value)

    encodeSerializableValue(EdgeSurrogate.serializer(tSerializer), surrogate)
}

private fun <T> Decoder.decodeEdge(tSerializer: KSerializer<T>): DefaultEdge<T> {
    val surrogate = decodeSerializableValue(EdgeSurrogate.serializer(tSerializer))

    return DefaultEdge(surrogate.node, DefaultConnectionCursor(surrogate.cursor))
}
