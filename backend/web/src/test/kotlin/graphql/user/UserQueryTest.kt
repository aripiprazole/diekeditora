package com.lorenzoog.diekeditora.web.graphql.user

import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.web.factories.UserFactory
import com.lorenzoog.diekeditora.web.utils.graphQL
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals

@SpringBootTest
class UserQueryTest(
    @Autowired private val json: Json,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: WebTestClient,
    @Autowired private val userFactory: UserFactory,
) {
    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        val pageNumber = 1
        val pageSize = 15
        val items = userRepository.findAll(pageNumber, pageSize).toList()

        val original = SimpleListConnection(items).get(newDataFetchingEnvironment().build())

        val connection = client.graphQL(UsersQuery) {
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
            client.graphQL(UserQuery) {
                variables = UserQuery.Variables(user.username)
            }
        )
    }
}
