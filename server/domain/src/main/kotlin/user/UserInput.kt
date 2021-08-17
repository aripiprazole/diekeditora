package com.diekeditora.domain.user

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
    init {
        validate(this) {
            validate(UserInput::name).hasSize(min = 4, max = 32).isNotBlank()
            validate(UserInput::username).hasSize(min = 4, max = 16).isNotBlank()
            validate(UserInput::email).isEmail()
            validate(UserInput::birthday).isLessThan(LocalDate.now())
        }
    }

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
