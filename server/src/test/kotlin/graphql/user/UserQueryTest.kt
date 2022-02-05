package com.diekeditora.tests.graphql.user

import com.diekeditora.redis.infra.CacheProvider
import com.diekeditora.tests.factories.UserFactory
import com.diekeditora.tests.graphql.GraphQLTestClient
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserService
import graphql.relay.Connection
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserQueryTest(
    @Autowired val userService: UserService,
    @Autowired val userFactory: UserFactory,
    @Autowired val cacheProvider: CacheProvider,
    @Autowired val client: GraphQLTestClient,
) {

    @Test
    fun `test should retrieve all users paginated`(): Unit = runBlocking {
        val first = 15
        val after = null
        val users = userFactory.createMany(first * 2)
            .forEach { user -> userService.saveUser(user) }
            .let { userService.findUsers(first) }

        cacheProvider
            .repo<Connection<User>>()
            .delete("userConnection.$first.$after")

        val response = client.request(UsersQuery(first)) {
            authenticate("user.view")
        }

        assertEquals(users, response)
    }

    @Test
    fun `test should retrieve user by username`(): Unit = runBlocking {
        val user = userService.saveUser(userFactory.create())

        val response = client.request(UserQuery(user.username)) {
            authenticate("user.view")
        }

        assertEquals(user, response)
    }
}
