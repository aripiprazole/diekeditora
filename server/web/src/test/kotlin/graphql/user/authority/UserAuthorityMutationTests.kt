package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class UserAuthorityMutationTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val userAuthorityRepository: UserAuthorityRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should update user's authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityFactory.create().also { userAuthorityRepository.save(user, it) }

        val authorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.request(UpdateUserAuthoritiesQuery) {
            authentication = auth.mock("authority.admin")
            variables = UpdateUserAuthoritiesQuery.Variables(
                username = user.username,
                authorities = authorities + authority.value,
            )
        }

        assertEquals(
            authorities + authority.value,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not add user's an authority without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        assertGraphQLForbidden {
            client.request(UpdateUserAuthoritiesQuery) {
                authentication = auth.mock()
                variables = UpdateUserAuthoritiesQuery.Variables(
                    username = user.username,
                    authorities = listOf(),
                )
            }
        }
    }
}
