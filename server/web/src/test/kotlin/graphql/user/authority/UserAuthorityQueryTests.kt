package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLException
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.NOT_ENOUGH_AUTHORITIES
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class UserAuthorityQueryTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val authorityRepository: AuthorityRepository,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val userAuthorityRepository: UserAuthorityRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve user's roles`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userAuthorityRepository.save(
                it,
                authorityRepository.saveAll(authorityFactory.createMany(5)).toList()
            )
        }

        val response = client.request(UserAuthoritiesQuery) {
            authentication = auth.mock("authorities.view")
            variables = UserAuthoritiesQuery.Variables(username = user.username)
        }

        assertEquals(userAuthorityRepository.findByUser(user).map { it.authority }, response)
    }

    @Test
    fun `test should not retrieve user's roles without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        assertThrows<GraphQLException>(NOT_ENOUGH_AUTHORITIES) {
            client.request(UserAuthoritiesQuery) {
                authentication = auth.mock()
                variables = UserAuthoritiesQuery.Variables(username = user.username)
            }
        }
    }
}
