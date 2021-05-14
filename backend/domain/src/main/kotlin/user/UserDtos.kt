package com.lorenzoog.diekeditora.domain.user

import com.expediagroup.graphql.generator.annotations.GraphQLName
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
@SerialName("UserCreateDto")
@GraphQLName("UserCreateInput")
data class UserCreateDto(
    val name: String,
    val username: String,
    val email: String,
    val password: String? = null,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
    val emailVerifiedAt: @Contextual LocalDateTime? = null,
) {
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            username = username,
            password = password,
            birthday = birthday,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            emailVerifiedAt = emailVerifiedAt,
        )
    }

    companion object {
        fun from(user: User): UserCreateDto {
            return UserCreateDto(
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
@SerialName("UserResponseDto")
data class UserResponseDto(
    val name: String,
    val username: String,
    val email: String,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime? = null,
    val updatedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
    val emailVerifiedAt: @Contextual LocalDateTime? = null
)
