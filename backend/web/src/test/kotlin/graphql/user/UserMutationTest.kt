package com.lorenzoog.diekeditora.web.graphql.user

import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.web.factories.UserFactory
import com.lorenzoog.diekeditora.web.utils.request
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class UserMutationTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: WebTestClient,
    @Autowired private val userFactory: UserFactory,
) {
    @Test
    fun `test should create user`(): Unit = runBlocking {
        var user = userFactory.create()

        val name = user.name
        val username = user.username
        val email = user.email
        val password = user.password
        val birthday = user.birthday

        client.request(
            """
            mutation {
               createUser(
                   name: $name,
                   username: $username,
                   email: $email,
                   password: $password,
                   birthday: $birthday
               ) {
                   name
                   username
                   email
                   birthday
                   createdAt
                   updatedAt
                   emailVerifiedAt
                   deletedAt
               }
            }
            """.trimIndent()
        )

        user = requireNotNull(userRepository.findByUsername(username)) {
            "User must be not null"
        }

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(password, user.password)
        assertNotNull(user.updatedAt)
        assertNotNull(user.emailVerifiedAt)
    }

    @Test
    fun `test should update user by username`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()

        val name = newUser.name
        val username = newUser.username
        val email = newUser.email
        val password = newUser.password

        client.request(
            """
            mutation {
               updateUser(
                   target: ${user.username},
                   name: $name,
                   username: $username,
                   email: $email,
                   password: $password
               ) {}
            }
            """.trimIndent()
        )

        user = requireNotNull(userRepository.findByUsername(username)) {
            "User must be not null"
        }

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(password, user.password)
        assertNotNull(user.updatedAt)
        assertNotNull(user.emailVerifiedAt)
    }
}
