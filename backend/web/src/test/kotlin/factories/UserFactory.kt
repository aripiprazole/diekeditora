package com.lorenzoog.diekeditora.web.tests.factories

import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.shared.generateRandomString
import io.github.serpro69.kfaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class UserFactory(val faker: Faker) : Factory<User> {
    override fun create(): User {
        return User(
            name = faker.name.name(),
            username = faker.name.firstName(),
            email = faker.internet.email(),
            birthday = LocalDate.now(),
            password = generateRandomString(16),
            emailVerifiedAt = LocalDateTime.now(),
        )
    }
}
