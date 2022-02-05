package com.diekeditora.tests.factories

import com.diekeditora.shared.infra.generateRandomString
import com.diekeditora.user.domain.User
import io.github.serpro69.kfaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UserFactory(val faker: Faker) : Factory<User> {
    override fun create(): User {
        return User(
            name = faker.name.name(),
            username = faker.name.firstName().padStart(12) + generateRandomString(4),
            email = faker.internet.email(),
            birthday = LocalDate.now(),
        )
    }
}
