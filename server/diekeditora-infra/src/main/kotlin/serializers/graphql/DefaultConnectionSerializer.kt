@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.infra.serializers.graphql

import graphql.relay.DefaultConnection
import graphql.relay.Edge
import graphql.relay.PageInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class DefaultConnectionSerializer<T>(
    private val tSerializer: KSerializer<T>
) : KSerializer<DefaultConnection<T>> {
    @Serializable
    @SerialName("Connection")
    data class Surrogate<T>(
        val edges: List<@Serializable(with = EdgeSerializer::class) Edge<T>>,
        val pageInfo: @Serializable(with = PageInfoSerializer::class) PageInfo
    )

    override val descriptor = Surrogate.serializer(tSerializer).descriptor

    override fun serialize(encoder: Encoder, value: DefaultConnection<T>) {
        val surrogate = Surrogate(value.edges, value.pageInfo)

        encoder.encodeSerializableValue(Surrogate.serializer(tSerializer), surrogate)
    }

    override fun deserialize(decoder: Decoder): DefaultConnection<T> {
        val surrogate = decoder.decodeSerializableValue(Surrogate.serializer(tSerializer))

        return DefaultConnection(surrogate.edges, surrogate.pageInfo)
    }
}
