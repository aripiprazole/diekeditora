package com.diekeditora.domain.user

import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class UserInput(
    val name: String,
    val username: String,
    val email: String,
    val birthday: LocalDate,
) {
    fun toUser(): User {
        return User(name = name, email = email, username = username, birthday = birthday)
    }

    companion object {
        fun from(user: User): UserInput {
            return UserInput(
                name = user.name,
                username = user.username,
                email = user.email,
                birthday = user.birthday ?: error("When creating user, could not miss birthday"),
            )
        }
    }
}
