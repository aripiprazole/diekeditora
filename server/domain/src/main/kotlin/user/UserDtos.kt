package com.diekeditora.domain.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
@SerialName("UserInput")
data class UserInput(
    val name: String,
    val username: String,
    val email: String,
    val password: String? = null,
    val birthday: @Contextual LocalDate,
) {
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            username = username,
            password = password,
            birthday = birthday,
        )
    }

    companion object {
        fun from(user: User): UserInput {
            return UserInput(
                name = user.name,
                username = user.username,
                email = user.email,
                password = user.password,
                birthday = user.birthday,
            )
        }
    }
}

@Serializable
@SerialName("UserPayload")
data class UserPayload(
    val name: String,
    val username: String,
    val email: String,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime? = null,
    val updatedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
)
