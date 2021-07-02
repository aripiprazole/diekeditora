package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserAuthorityQueryTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val authorityService: AuthorityService,
    @Autowired val userAuthorityRepository: UserAuthorityRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve user's authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userAuthorityRepository.link(it, authorityFactory.createMany(5))
        }

        val response = client.request(UserAuthoritiesQuery) {
            authentication = auth.mock("user.view", "authority.view")
            variables = UserAuthoritiesQuery.Variables(username = user.username)
        }

        assertEquals(
            userAuthorityRepository.findByUser(user).map { it.value }.toList(),
            response.authorities
        )
    }

    @Test
    fun `test should retrieve all user's authorities`(): Unit = runBlocking {
        userRepository.save(userFactory.create()).also {
            userAuthorityRepository.link(it, authorityFactory.createMany(5))
        }

        val user = userRepository.save(userFactory.create()).also {
            userAuthorityRepository.link(it, authorityFactory.createMany(5))
        }

        val response = client.request(UserAuthoritiesQuery) {
            authentication = auth.mock("user.view", "authority.view")
            variables = UserAuthoritiesQuery.Variables(username = user.username)
        }

        assertEquals(
            authorityService.findAllAuthoritiesByUser(user).toList(),
            response.allAuthorities
        )
    }

    @Test
    fun `test should not retrieve all user's authorities`(): Unit = runBlocking {
        userRepository.save(userFactory.create()).also {
            userAuthorityRepository.link(it, authorityFactory.createMany(5))
        }

        val user = userRepository.save(userFactory.create()).also {
            userAuthorityRepository.link(it, authorityFactory.createMany(5))
        }

        assertGraphQLForbidden {
            client.request(UserAuthoritiesQuery) {
                authentication = auth.mock()
                variables = UserAuthoritiesQuery.Variables(username = user.username)
            }
        }
    }

    @Test
    fun `test should not retrieve user's authorities without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        assertGraphQLForbidden {
            client.request(UserAuthoritiesQuery) {
                authentication = auth.mock()
                variables = UserAuthoritiesQuery.Variables(username = user.username)
            }
        }
    }
}
