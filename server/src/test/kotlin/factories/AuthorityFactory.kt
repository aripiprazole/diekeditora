package com.diekeditora.tests.factories

import com.diekeditora.shared.infra.generateRandomString
import org.springframework.stereotype.Component

@Component
class AuthorityFactory : Factory<String> {
    override fun create(): String {
        return generateRandomString(4) + "." + generateRandomString(4)
    }
}
