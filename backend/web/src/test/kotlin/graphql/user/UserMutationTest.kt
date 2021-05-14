package com.lorenzoog.diekeditora.web.tests.graphql.user

import com.lorenzoog.diekeditora.domain.user.UserCreateDto
import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.web.graphql.user.UpdateUserInput
import com.lorenzoog.diekeditora.web.tests.factories.UserFactory
import com.lorenzoog.diekeditora.web.tests.utils.graphQL
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class UserMutationTest(
    @Autowired private val json: Json,
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

        user = client
            .graphQL(json, CreateUserMutation) {
                variables = UserCreateDto.from(user)
            }
            .user.let(::assertNotNull)

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(password, user.password)
        assertEquals(birthday, user.birthday)
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

        user = client
            .graphQL(json, UpdateUserMutation) {
                variables = UpdateUserInput(user.username, UserCreateDto.from(newUser))
            }
            .user.let(::assertNotNull)

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(password, user.password)
        assertNotNull(user.updatedAt)
        assertNotNull(user.emailVerifiedAt)
    }
}
