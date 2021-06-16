@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.infra.serializers.graphql

import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultPageInfo
import graphql.relay.PageInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
@SerialName("PageInfo")
data class PageInfoSurrogate(
    val startCursor: String,
    val endCursor: String,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)

@Serializer(forClass = PageInfo::class)
object PageInfoSerializer {
    override val descriptor = PageInfoSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: PageInfo) {
        encoder.encodePageInfo(value)
    }

    override fun deserialize(decoder: Decoder): PageInfo {
        return decoder.decodePageInfo()
    }
}

@Serializer(forClass = DefaultPageInfo::class)
object DefaultPageInfoSerializer {
    override val descriptor = PageInfoSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DefaultPageInfo) {
        encoder.encodePageInfo(value)
    }

    override fun deserialize(decoder: Decoder): DefaultPageInfo {
        return decoder.decodePageInfo()
    }
}

private fun Encoder.encodePageInfo(value: PageInfo) {
    val surrogate = PageInfoSurrogate(
        value.startCursor.value,
        value.endCursor.value,
        value.isHasPreviousPage,
        value.isHasNextPage
    )

    encodeSerializableValue(PageInfoSurrogate.serializer(), surrogate)
}

private fun Decoder.decodePageInfo(): DefaultPageInfo {
    val surrogate = decodeSerializableValue(PageInfoSurrogate.serializer())

    return DefaultPageInfo(
        DefaultConnectionCursor(surrogate.startCursor),
        DefaultConnectionCursor(surrogate.endCursor),
        surrogate.hasPreviousPage,
        surrogate.hasNextPage
    )
}
