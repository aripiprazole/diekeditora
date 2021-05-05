@file:OptIn(ExperimentalSerializationApi::class)

package com.lorenzoog.diekeditora.domain.user

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = User::class)
object UserSerializer {
    override val descriptor = UserResponseDto.serializer().descriptor

    override fun serialize(encoder: Encoder, value: User) {
        encoder.encodeSerializableValue(
            UserResponseDto.serializer(),
            UserResponseDto(
                value.name,
                value.username,
                value.email,
                value.birthday,
                value.createdAt,
                value.updatedAt,
                value.deletedAt,
                value.emailVerifiedAt
            )
        )
    }

    override fun deserialize(decoder: Decoder): User {
        val dto = decoder.decodeSerializableValue(UserResponseDto.serializer())

        return User(
            name = dto.name,
            email = dto.email,
            username = dto.username,
            birthday = dto.birthday,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            deletedAt = dto.updatedAt,
            emailVerifiedAt = dto.emailVerifiedAt
        )
    }
}
