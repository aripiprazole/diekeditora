package com.diekeditora.web.tests.factories

import com.diekeditora.infra.entities.Authority
import com.diekeditora.shared.generateRandomString
import org.springframework.stereotype.Component

@Component
class AuthorityFactory : Factory<Authority> {
    override fun create(): Authority {
        return Authority(value = generateRandomString(4) + "." + generateRandomString(4))
    }
}
