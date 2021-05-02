package com.lorenzoog.diekeditora.factories

import com.lorenzoog.diekeditora.dtos.Secret
import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.utils.generateRandomString
import io.github.serpro69.kfaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class UserFactory(val faker: Faker) : Factory<User> {
    override fun generateEntity(): User {
        return User(
            name = faker.name.name(),
            username = faker.name.firstName(),
            email = faker.internet.email(),
            birthday = LocalDate.now(),
            password = Secret.of(generateRandomString(16)),
            emailVerifiedAt = LocalDateTime.now()
        )
    }
}
