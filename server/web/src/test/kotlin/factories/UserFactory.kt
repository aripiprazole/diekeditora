package com.diekeditora.web.tests.factories

import com.diekeditora.domain.user.User
import io.github.serpro69.kfaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UserFactory(val faker: Faker) : Factory<User> {
    override fun create(): User {
        return User(
            name = faker.name.name(),
            username = faker.name.firstName(),
            email = faker.internet.email(),
            birthday = LocalDate.now(),
        )
    }
}
