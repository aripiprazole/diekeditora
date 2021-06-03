package com.diekeditora.web.tests.graphql.user

import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserQueryTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val userFactory: UserFactory,
    @Autowired private val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        val pageNumber = 1
        val pageSize = 15
        val items = userRepository.findAll(pageNumber, pageSize).toList()

        val original = SimpleListConnection(items).get(newDataFetchingEnvironment().build())

        val connection = client.request(UsersQuery) {
            authentication = auth.mock("users.view")
            variables = UsersQuery.Variables(pageNumber)
        }

        connection.edges.forEachIndexed { index, edge ->
            val originalEdge = original.edges[index]

            assertEquals(originalEdge.node, edge.node)
            assertEquals(originalEdge.cursor, edge.cursor)
        }

        assertEquals(original.pageInfo.startCursor, connection.pageInfo.startCursor)
        assertEquals(original.pageInfo.endCursor, connection.pageInfo.endCursor)
        assertEquals(original.pageInfo.isHasNextPage, connection.pageInfo.isHasNextPage)
        assertEquals(original.pageInfo.isHasPreviousPage, connection.pageInfo.isHasPreviousPage)
    }

    @Test
    fun `test should retrieve an user`(): Unit = runBlocking {
        val user = userFactory.create()
            .let { userRepository.save(it) }
            .let { userRepository.findByUsername(it.username) }
            .let(::requireNotNull)

        assertEquals(
            user,
            client.request(UserQuery) {
                authentication = auth.mock("users.view")
                variables = UserQuery.Variables(user.username)
            }
        )
    }
}
