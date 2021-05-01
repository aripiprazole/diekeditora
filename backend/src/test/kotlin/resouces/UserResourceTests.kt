package com.lorenzoog.diekeditora.resouces

import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.factories.UserFactory
import com.lorenzoog.diekeditora.repositories.UserRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserResourceTests(
    @Autowired val json: Json,
    @Autowired val client: WebTestClient,
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory
) {

    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(userFactory.generateEntity())

        client.get().uri("/users", mapOf("page" to 1))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<List<User>>()
            .isEqualTo(
                userRepository.findAll(1).toList().onEach {
                    assertNotNull(it.emailVerifiedAt)
                    assertNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
    }

    @Test
    fun `test should retrieve an user`(): Unit = runBlocking {
        val user = userFactory.generateEntity().let { userRepository.save(it) }

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
        val exchange = client.post().uri("/users").bodyValue(userFactory.generateEntity())
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<User>()
            .returnResult()

        val body = requireNotNull(exchange.responseBody) { "User username must be not null" }
        val user = requireNotNull(userRepository.findByUsername(body.username)) { "User must be not null" }

        assertEquals(user.username, body.username)
        assertEquals(user.name, body.name)
        assertEquals(user.birthday, body.birthday)
        assertNotNull(user.emailVerifiedAt)
        assertNull(user.updatedAt)
        assertNull(user.deletedAt)
    }

    @Test
    fun `test should update an user by requests body`(): Unit = runBlocking {
        var user = userFactory.generateEntity().let { userRepository.save(it) }
        val newUser = userFactory.generateEntity()

        val id = requireNotNull(user.id) { "User's id must be not null" }

        client.patch().uri("/users/${user.username}").bodyValue(newUser)
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertEquals(user.username, newUser.username)
        assertEquals(user.name, newUser.name)
        assertEquals(user.birthday, newUser.birthday)
        assertNotNull(user.emailVerifiedAt)
        assertNotNull(user.updatedAt)
        assertNull(user.deletedAt)
    }

    @Test
    fun `test should delete an user by its username`(): Unit = runBlocking {
        var user = userFactory.generateEntity().let { userRepository.save(it) }

        val id = requireNotNull(user.id) { "User's id must be not null" }

        client.delete().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertNotNull(user.deletedAt)
    }
}
