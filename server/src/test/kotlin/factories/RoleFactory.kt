package com.diekeditora.tests.factories

import com.diekeditora.role.domain.Role
import io.github.serpro69.kfaker.Faker
import org.springframework.stereotype.Component

@Component
class RoleFactory(val faker: Faker) : Factory<Role> {
    override fun create(): Role {
        return Role(name = faker.funnyName.name())
    }
}
