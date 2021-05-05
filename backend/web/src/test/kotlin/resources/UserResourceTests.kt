package com.lorenzoog.diekeditora.web.resources

import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.web.factories.UserFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserResourceTests(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: WebTestClient,
    @Autowired private val userFactory: UserFactory,
) {
    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        val page = 1
        val users = userRepository.findAll(page).toList().onEach {
            assertNotNull(it.emailVerifiedAt)
            assertNull(it.updatedAt)
            assertNull(it.deletedAt)
        }
        val pageable = PageRequest.of(1, 15)

        client.get().uri("/users", mapOf("page" to 1))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<Page<User>>()
            .isEqualTo(
                PageImpl(users, pageable, userRepository.estimateTotalUsers())
            )
    }

    @Test
    fun `test should retrieve an user`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }

        client.get().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<User>()
            .isEqualTo(
                requireNotNull(userRepository.findByUsername(user.username)).also {
                    assertNotNull(it.emailVerifiedAt)
                    assertNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
    }

    @Test
    fun `test should store an user`(): Unit = runBlocking {
        @Serializable
        data class Data(
            val username: String,
            val email: String,
            val name: String,
            val password: String,
            val birthday: @Contextual LocalDate
        )

        val value = userFactory.create().run {
            Data(username, email, name, requireNotNull(password), birthday)
        }

        val exchange = client.post().uri("/users")
            .bodyValue(value)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<User>()
            .returnResult()

        val body = requireNotNull(exchange.responseBody) { "User username must be not null" }
        val user = requireNotNull(userRepository.findByUsername(body.username)) {
            "User must be not null"
        }

        assertEquals(user.username, body.username)
        assertEquals(user.name, body.name)
        assertEquals(user.birthday, body.birthday)
        assertNotNull(user.emailVerifiedAt)
        assertNull(user.updatedAt)
        assertNull(user.deletedAt)
    }

    @Test
    fun `test should update an user by requests body`(): Unit = runBlocking {
        @Serializable
        data class Data(
            val username: String,
            val email: String,
            val name: String,
            val password: String,
            val birthday: @Contextual LocalDate
        )

        var user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()
        val id = requireNotNull(user.id) { "User's id must be not null" }

        val value = newUser.run {
            Data(username, email, name, requireNotNull(password), birthday)
        }

        client.patch().uri("/users/${user.username}").bodyValue(value)
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertEquals(newUser.username, user.username)
        assertEquals(newUser.name, user.name)
        assertEquals(newUser.birthday, user.birthday)
        assertNotNull(user.updatedAt)
        assertNull(user.deletedAt)
    }

    @Test
    fun `test should delete an user by its username`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }

        val id = requireNotNull(user.id) { "User's id must be not null" }

        client.delete().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertNotNull(user.deletedAt)
    }
}
