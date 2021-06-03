package com.diekeditora.web.tests.resources

import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
class AuthorityResourceTests(
    @Autowired val authorityRepository: AuthorityRepository,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val client: WebTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve all authorities`(): Unit = runBlocking {
        authorityRepository.save(authorityFactory.create())

        client.mutateWith(auth.configure("authority.view"))
            .get().uri("/roles")
            .exchange()
            .expectStatus().isOk
            .expectBody<Set<String>>()
            .isEqualTo(authorityRepository.findAll().map { it.value }.toSet())
    }

    @Test
    fun `test should not retrieve paginated roles without authorities`(): Unit = runBlocking {
        authorityRepository.save(authorityFactory.create())

        client.mutateWith(auth.configure())
            .get().uri("/roles")
            .exchange()
            .expectStatus().isForbidden
    }
}
