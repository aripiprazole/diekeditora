package com.lorenzoog.diekeditora.web.graphql.user

import com.expediagroup.graphql.server.types.GraphQLResponse
import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.web.factories.UserFactory
import com.lorenzoog.diekeditora.web.utils.request
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserQueryTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val client: WebTestClient,
    @Autowired private val userFactory: UserFactory,
) {
    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }

        client
            .request(
                """
                query {
                    user(username: ${user.username}) {
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
            .expectBody<GraphQLResponse<User?>>()
            .isEqualTo(
                GraphQLResponse(
                    requireNotNull(userRepository.findByUsername(user.username)).also {
                        assertNotNull(it.emailVerifiedAt)
                        assertNull(it.updatedAt)
                        assertNull(it.deletedAt)
                    }
                )
            )
    }

    @Test
    fun `test should retrieve an user`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        val page = 1
        val users = userRepository.findAll(page).toList().onEach {
            assertNotNull(it.emailVerifiedAt)
            assertNull(it.updatedAt)
            assertNull(it.deletedAt)
        }
        val pageable = PageRequest.of(1, 15)

        client
            .request(
                """
                query {
                    users(page: 1) {
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
            .expectBody<GraphQLResponse<Page<User>>>()
            .isEqualTo(
                GraphQLResponse(
                    data = PageImpl(users, pageable, userRepository.estimateTotalUsers())
                )
            )
    }
}
