package com.diekeditora.domain.user

import com.diekeditora.domain.graphql.Email
import com.diekeditora.domain.graphql.Max
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank
import com.diekeditora.domain.graphql.Past
import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class UserInput(
    @NotBlank @Max(32) @Min(4) val name: String,
    @NotBlank @Max(16) @Min(4) val username: String,
    @Email val email: String,
    @Past val birthday: LocalDate,
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
