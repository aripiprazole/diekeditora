package com.diekeditora.web.tests.utils

import com.diekeditora.web.tests.factories.UserFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class AuthenticationMocker(val userFactory: UserFactory) {

    fun mock(
        authorities: List<String> = emptyList(),
        roles: List<String> = emptyList()
    ): Authentication {
        return UsernamePasswordAuthenticationToken(
            userFactory.create(),
            null,
            authorities.map { SimpleGrantedAuthority(it) }
        )
    }
}
