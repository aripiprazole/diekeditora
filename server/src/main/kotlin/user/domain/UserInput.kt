package com.diekeditora.user.domain

import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isNotBlank
import org.valiktor.validate
import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class UserInput(
    val name: String,
    val username: String,
    val email: String,
    val birthday: LocalDate,
) {
    fun toUser(): User =
        validate(User(name = name, email = email, username = username, birthday = birthday)) {
            validate(User::name).hasSize(min = 4, max = 32).isNotBlank()
            validate(User::username).hasSize(min = 4, max = 16).isNotBlank()
            validate(User::email).isEmail()
            validate(User::birthday).isLessThan(LocalDate.now())
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
