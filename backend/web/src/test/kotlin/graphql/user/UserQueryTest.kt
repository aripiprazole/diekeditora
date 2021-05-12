package com.lorenzoog.diekeditora.web.graphql.user

import com.expediagroup.graphql.server.types.GraphQLResponse
import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.web.factories.UserFactory
import com.lorenzoog.diekeditora.web.utils.request
import graphql.relay.DefaultConnection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserQueryTest(
    @Autowired private val json: Json,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: WebTestClient,
    @Autowired private val userFactory: UserFactory,
) {
    @Test
    fun `test should retrieve an user`(): Unit = runBlocking {
        val user = userFactory.create()
            .let { userRepository.save(it) }
            .let { userRepository.findByUsername(it.username) }
            .let(::requireNotNull)

        client
            .request(
                """
                query {
                    user(username: "${user.username}") {
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
            .exchange()
            .expectBody<GraphQLResponse<Map<String, User?>>>()
            .isEqualTo(GraphQLResponse(mapOf("user" to user)))
    }

    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        val pageNumber = 1
        val pageSize = 15
        val items = userRepository.findAll(pageNumber, pageSize).toList().onEach {
            assertNull(it.updatedAt)
            assertNull(it.deletedAt)
        }

        val original = SimpleListConnection(items).get(newDataFetchingEnvironment().build())

        client
            .request(
                """
                query {
                    users(page: 1) {
                        pageInfo {
                            startCursor
                            endCursor
                            hasNextPage
                            hasPreviousPage
                        }
                        edges {
                            cursor
                            node {
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
                    }
                }
                """.trimIndent()
            )
            .exchange()
            .expectBody<JsonObject>()
            .consumeWith { exchange ->
                val users = exchange.responseBody
                    ?.get("data")?.jsonObject
                    ?.get("users")
                    ?.let { json.decodeFromJsonElement<DefaultConnection<User>>(it) }
                    .let(::assertNotNull)

                users.edges.forEachIndexed { index, edge ->
                    val originalEdge = original.edges[index]

                    assertEquals(originalEdge.node, edge.node)
                    assertEquals(originalEdge.cursor, edge.cursor)
                }

                assertEquals(original.pageInfo.startCursor, users.pageInfo.startCursor)
                assertEquals(original.pageInfo.endCursor, users.pageInfo.endCursor)
                assertEquals(original.pageInfo.isHasNextPage, users.pageInfo.isHasNextPage)
                assertEquals(original.pageInfo.isHasPreviousPage, users.pageInfo.isHasPreviousPage)
            }
    }
}
