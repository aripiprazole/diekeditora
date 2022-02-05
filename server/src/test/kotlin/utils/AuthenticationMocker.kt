package com.diekeditora.tests.utils

import com.diekeditora.tests.factories.UserFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClientConfigurer

@Component
class AuthenticationMocker(val userFactory: UserFactory) {
    fun configure(
        vararg authorities: String
    ): WebTestClientConfigurer = mockAuthentication(mock(authorities = authorities))

    fun mock(vararg authorities: String): Authentication {
        return UsernamePasswordAuthenticationToken(
            userFactory.create(),
            null,
            authorities.map { SimpleGrantedAuthority(it) }
        )
    }
}
