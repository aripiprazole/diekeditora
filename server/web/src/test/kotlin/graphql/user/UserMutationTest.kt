package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.user.UserInput
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.graphql.user.UpdateUserInput
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class UserMutationTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val userFactory: UserFactory,
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
    fun `test should update user by username`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()

        val name = newUser.name
        val username = newUser.username
        val email = newUser.email

        user = client
            .request(UpdateUserMutation) {
                variables = UpdateUserMutation.Variables(
                    input = UpdateUserInput(user.username, UserInput.from(newUser)),
                )
            }
            .user.let(::assertNotNull)

        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
    }
}
