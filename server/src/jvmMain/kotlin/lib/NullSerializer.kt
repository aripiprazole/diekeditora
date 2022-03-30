@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.lib

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NullSerializer : KSerializer<Any?> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Any?", PrimitiveKind.STRING).nullable

  override fun serialize(encoder: Encoder, value: Any?) {
    encoder.encodeNull()
  }

  override fun deserialize(decoder: Decoder): Any? {
    return decoder.decodeNull()
  }
}
