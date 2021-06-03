package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.user.UserInput
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.graphql.user.UpdateUserInput
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLException
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.NotEnoughAuthorities
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class UserMutationTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val userFactory: UserFactory,
    @Autowired private val auth: AuthenticationMocker,
) {

    @Test
    fun `test should create user`(): Unit = runBlocking {
        var user = userFactory.create()

        val name = user.name
        val username = user.username
        val email = user.email
        val birthday = user.birthday

        user = client
            .request(CreateUserMutation) {
                authentication = auth.mock("users.store")
                variables = CreateUserMutation.Variables(
                    input = UserInput.from(user),
                )
            }
            .user

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(birthday, user.birthday)
    }

    @Test
    fun `test should not create user without authorities`(): Unit = runBlocking {
        val user = userFactory.create()

        assertThrows<GraphQLException>(NotEnoughAuthorities) {
            client.request(CreateUserMutation) {
                authentication = auth.mock()
                variables = CreateUserMutation.Variables(
                    input = UserInput.from(user),
                )
            }
        }
    }

    @Test
    fun `test should update user by username`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()

        val name = newUser.name
        val username = newUser.username
        val email = newUser.email

        user = client
            .request(UpdateUserMutation) {
                authentication = auth.mock("users.update")
                variables = UpdateUserMutation.Variables(
                    input = UpdateUserInput(user.username, UserInput.from(newUser)),
                )
            }
            .user.let(::assertNotNull)

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
    }

    @Test
    fun `test should not update user by username without authorities`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()

        assertThrows<GraphQLException>(NotEnoughAuthorities) {
            client.request(UpdateUserMutation) {
                authentication = auth.mock()
                variables = UpdateUserMutation.Variables(
                    input = UpdateUserInput(user.username, UserInput.from(newUser)),
                )
            }
        }
    }
}
