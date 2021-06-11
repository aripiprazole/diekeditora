@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.authority

import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
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
    @Autowired private val authorityRepository: AuthorityRepository,
    @Autowired private val authorityFactory: AuthorityFactory,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve all authorities`(): Unit = runBlocking {
        authorityRepository.saveAll(authorityFactory.createMany(15))

        val response = client.request(AuthoritiesQuery) {
            authentication = auth.mock("authority.view")
            variables = AuthoritiesQuery.Variables
        }

        assertEquals(authorityRepository.findAll().map { it.authority }.toSet(), response)
    }

    @Test
    fun `test not should retrieve all authorities without authorities`(): Unit = runBlocking {
        authorityRepository.saveAll(authorityFactory.createMany(15))

        assertGraphQLForbidden {
            client.request(AuthoritiesQuery) {
                authentication = auth.mock("authority.view")
                variables = AuthoritiesQuery.Variables
            }
        }
    }
}

object AuthoritiesQuery :
    TestQuery<AuthoritiesQuery.Variables, Set<String>>(typeOf<Set<String>>()) {
    override val queryName = "authorities"
    override val operationName = "AuthoritiesQuery"
    override val query = """
        query AuthoritiesQuery {
            authorities() {
            }
        }
    """.trimIndent()

    @Serializable
    object Variables
}
