@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.authority

import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.TestQuery
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.reflect.typeOf
import kotlin.test.assertEquals

@SpringBootTest
class AuthorityQueryTests(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val authorityRepository: AuthorityRepository,
    @Autowired private val userAuthorityRepository: UserAuthorityRepository,
    @Autowired private val authorityFactory: AuthorityFactory,
    @Autowired private val userFactory: UserFactory,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve all authorities`(): Unit = runBlocking {
        userRepository.save(userFactory.create()).also { user ->
            userAuthorityRepository.link(user, authorityFactory.createMany(15))
        }

        val response = client.request(AuthoritiesQuery) {
            authentication = auth.mock("authority.view")
            variables = AuthoritiesQuery.Variables
        }

        assertEquals(authorityRepository.findAll().map { it.value }.toSet(), response)
    }

    @Test
    fun `test not should retrieve all authorities without authorities`(): Unit = runBlocking {
        userRepository.save(userFactory.create()).also { user ->
            userAuthorityRepository.link(user, authorityFactory.createMany(15))
        }

        assertGraphQLForbidden {
            client.request(AuthoritiesQuery) {
                authentication = auth.mock()
                variables = AuthoritiesQuery.Variables
            }
        }
    }
}

object AuthoritiesQuery : TestQuery<AuthoritiesQuery.Variables, Set<String>>(
    typeOf<Set<String>>()
) {
    override val queryName = "authorities"
    override val operationName = "AuthoritiesQuery"
    override val query = """
        query AuthoritiesQuery {
            authorities
        }
    """.trimIndent()

    @Serializable
    object Variables
}
