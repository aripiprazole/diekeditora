@file:OptIn(ExperimentalSerializationApi::class)

package com.lorenzoog.diekeditora.domain.user

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = User::class)
object UserSerializer {
    override val descriptor = UserPayload.serializer().descriptor

    override fun serialize(encoder: Encoder, value: User) {
        val serializable = UserPayload(
            value.name,
            value.username,
            value.email,
            value.birthday,
            value.createdAt,
            value.updatedAt,
            value.deletedAt,
            value.emailVerifiedAt
        )

        encoder.encodeSerializableValue(UserPayload.serializer(), serializable)
    }

    override fun deserialize(decoder: Decoder): User {
        return decoder.decodeSerializableValue(UserInput.serializer()).toUser()
    }
}
