package com.diekeditora.app.tests.factories

import com.google.firebase.auth.FirebaseToken
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.mockk
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TokenFactory(val faker: Faker) : Factory<FirebaseToken> {
    override fun create(): FirebaseToken = mockk {
        every { issuer } returns "GOOGLE"
        every { name } returns faker.name.name()
        every { uid } returns UUID.randomUUID().toString()
        every { tenantId } returns UUID.randomUUID().toString()
        every { email } returns faker.internet.email()
        every { picture } returns faker.internet.domain()
        every { isEmailVerified } returns true
    }
}
