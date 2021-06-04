package com.diekeditora.web.tests.resources

import com.diekeditora.domain.user.UserAddAuthorityDto
import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertEquals

@SpringBootTest
class UserAuthorityResourceTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val authorityRepository: AuthorityRepository,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val userAuthorityRepository: UserAuthorityRepository,
    @Autowired val client: WebTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should add user's an authority`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create()).value

        val userAuthorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.mutateWith(auth.configure("authority.admin"))
            .post().uri("/users/${user.username}/authorities")
            .bodyValue(UserAddAuthorityDto(setOf(authority)))
            .exchange()
            .expectStatus().isNoContent

        assertEquals(
            userAuthorities + authority,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not add user's an authority without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create()).value

        client.mutateWith(auth.configure("authority.admin"))
            .post().uri("/users/${user.username}/authorities")
            .bodyValue(UserAddAuthorityDto(setOf(authority)))
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should retrieve user's authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userAuthorityRepository.save(it, authorityFactory.createMany(5))
        }

        client.mutateWith(auth.configure("authority.view"))
            .get().uri("/users/${user.username}/authorities")
            .exchange()
            .expectStatus().isOk
            .expectBody<Flow<String>>()
            .isEqualTo(userAuthorityRepository.findByUser(user).map { it.value })
    }

    @Test
    fun `test should not retrieve user's authorities without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        client.mutateWith(auth.configure())
            .get().uri("/users/${user.username}/authorities")
            .exchange()
            .expectStatus().isForbidden
    }
}
