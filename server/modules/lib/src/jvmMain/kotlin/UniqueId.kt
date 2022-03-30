@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.lib

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

@Serializable(UniqueIdSerializer::class)
sealed interface UniqueId {
  class Value(val id: String) : UniqueId
  object New : UniqueId
}

object UniqueIdSerializer : KSerializer<UniqueId> {
  override val descriptor: SerialDescriptor = SerialDescriptor("UniqueId", serialDescriptor<String?>())

  override fun serialize(encoder: Encoder, value: UniqueId) {
    when (value) {
      is UniqueId.Value -> encoder.encodeString(value.id)
      is UniqueId.New -> encoder.encodeNull()
    }
  }

  override fun deserialize(decoder: Decoder): UniqueId {
    return when (val id = decoder.decodeNullableSerializableValue(serializer<String?>())) {
      is String -> UniqueId.Value(id)
      else -> UniqueId.New
    }
  }
}
