@file:OptIn(ExperimentalSerializationApi::class)

package com.diekeditora.domain.user

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

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
        )

        encoder.encodeSerializableValue(UserPayload.serializer(), serializable)
    }

    override fun deserialize(decoder: Decoder): User {
        val surrogate = decoder.decodeSerializableValue(UserPayload.serializer())

        return User(
            name = surrogate.name,
            email = surrogate.email,
            username = surrogate.username,
            birthday = surrogate.birthday,
            createdAt = surrogate.createdAt ?: LocalDateTime.now(),
            updatedAt = surrogate.updatedAt,
            deletedAt = surrogate.deletedAt
        )
    }
}
