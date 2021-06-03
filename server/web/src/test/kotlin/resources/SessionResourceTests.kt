package com.diekeditora.web.tests.resources

import com.diekeditora.domain.user.User
import com.diekeditora.web.tests.utils.AuthenticationMocker
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
@ActiveProfiles("test")
class SessionResourceTests(
    @Autowired val auth: AuthenticationMocker,
    @Autowired val client: WebTestClient,
) {
    @Test
    fun `test should show current session with email and password login`() {
        val token = auth.mock()

        client.mutateWith(mockAuthentication(token))
            .get().uri("/session")
            .exchange()
            .expectBody<User>().isEqualTo(token.principal as User)
    }

    @Test
    fun `test should not show current session with email and password login not logged-in`() {
        client.get().uri("/session")
            .exchange()
            .expectStatus().isForbidden
    }
}
