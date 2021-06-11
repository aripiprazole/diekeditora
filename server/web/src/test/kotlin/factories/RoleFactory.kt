package com.diekeditora.web.tests.factories

import com.diekeditora.domain.authority.Role
import io.github.serpro69.kfaker.Faker
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class RoleFactory(val faker: Faker, val authorityFactory: AuthorityFactory) : Factory<Role> {
    override fun create(): Role {
        return Role(
            name = faker.funnyName.name(),
            authorities = authorityFactory.createMany(Random.nextInt(5)).map { it.authority }.toSet()
        )
    }
}
