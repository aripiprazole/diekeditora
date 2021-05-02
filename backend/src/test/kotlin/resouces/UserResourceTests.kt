package com.lorenzoog.diekeditora.resouces

import com.lorenzoog.diekeditora.dtos.Page
import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.factories.UserFactory
import com.lorenzoog.diekeditora.repositories.UserRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserResourceTests(
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
            .expectBody<Page<User>>()
            .isEqualTo(
                Page(
                    totalCount = 0,
                    totalPages = 0,
                    items = userRepository.findAll(1).toList().onEach {
                        assertNotNull(it.emailVerifiedAt)
                        assertNull(it.updatedAt)
                        assertNull(it.deletedAt)
                    }
                )
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
        @Serializable
        data class Data(
            val username: String,
            val email: String,
            val name: String,
            val password: String,
            val birthday: @Contextual LocalDate
        )

        val value = userFactory.generateEntity().run {
            Data(username, email, name, password.unwrap(), birthday)
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

        var user = userFactory.generateEntity().let { userRepository.save(it) }
        val newUser = userFactory.generateEntity()
        val id = requireNotNull(user.id) { "User's id must be not null" }

        val value = newUser.run {
            Data(username, email, name, password.unwrap(), birthday)
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
        var user = userFactory.generateEntity().let { userRepository.save(it) }

        val id = requireNotNull(user.id) { "User's id must be not null" }

        client.delete().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertNotNull(user.deletedAt)
    }
}
