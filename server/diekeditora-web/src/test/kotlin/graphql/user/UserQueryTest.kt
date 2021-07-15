package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.user.UserService
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserQueryTest(
    @Autowired val userService: UserService,
    @Autowired val userFactory: UserFactory,
    @Autowired val client: GraphQLTestClient,
) {

    @Test
    fun `test should retrieve all users paginated`(): Unit = runBlocking {
        val first = 15
        val users = userFactory.createMany(first * 2)
            .forEach { user -> userService.saveUser(user) }
            .let { userService.findUsers(first) }

        val response = client.request(UsersQuery(first)) {
            authenticate("user.view")
        }

        assertEquals(users, response)
    }

    @Test
    fun `test should retrieve user by username`(): Unit = runBlocking {
        val user = userFactory.create()

        val response = client.request(UserQuery(user.username)) {
            authenticate("user.view")
        }

        assertEquals(user, response)
    }
}
