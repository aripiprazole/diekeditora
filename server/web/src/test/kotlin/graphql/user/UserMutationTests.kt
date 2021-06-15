package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.user.UserInput
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class UserMutationTests(
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

        user = client.request(CreateUserMutation) {
            authentication = auth.mock("user.store")
            variables = CreateUserMutation.Variables(
                input = UserInput.from(user),
            )
        }

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(birthday, user.birthday)
    }

    @Test
    fun `test should not create user without authorities`(): Unit = runBlocking {
        val user = userFactory.create()

        assertGraphQLForbidden {
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

        user = client.request(UpdateUserMutation) {
            authentication = auth.mock("user.update")
            variables = UpdateUserMutation.Variables(
                username = user.username,
                input = UserInput.from(newUser),
            )
        }.let(::assertNotNull)

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
    }

    @Test
    fun `test should not update user by username without authorities`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()

        assertGraphQLForbidden {
            client.request(UpdateUserMutation) {
                authentication = auth.mock()
                variables = UpdateUserMutation.Variables(
                    username = user.username,
                    input = UserInput.from(newUser)
                )
            }
        }
    }

    @Test
    fun `test should delete user by username`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }

        user = client.request(DeleteUserMutation) {
            authentication = auth.mock("user.destroy")
            variables = DeleteUserMutation.Variables(user.username)
        }.let(::assertNotNull)

        assertNotNull(user.deletedAt)
    }

    @Test
    fun `test should not delete user by username without authorities`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }

        assertGraphQLForbidden {
            client.request(DeleteUserMutation) {
                authentication = auth.mock()
                variables = DeleteUserMutation.Variables(user.username)
            }
        }
    }
}
