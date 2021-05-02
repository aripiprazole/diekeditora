@file:OptIn(ExperimentalSerializationApi::class)

package com.lorenzoog.diekeditora.serializers

import com.lorenzoog.diekeditora.dtos.Secret
import com.lorenzoog.diekeditora.dtos.asSecret
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

@Serializer(forClass = Secret::class)
object SecretSerializer {
    override val descriptor = PrimitiveSerialDescriptor("Secret", PrimitiveKind.STRING).nullable

    override fun serialize(encoder: Encoder, value: Secret) {
        encoder.encodeNull()
    }

    override fun deserialize(decoder: Decoder): Secret {
        val deserializer = decoder.serializersModule.serializer<String?>()
        val value = decoder.decodeNullableSerializableValue(deserializer)

        return value.asSecret()
    }
}
