package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
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
    fun `test should link an authority to user`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityFactory.create()

        val authorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.request(LinkAuthoritiesToUserMutation) {
            authentication = auth.mock("authority.admin", "authority.view")
            variables = LinkAuthoritiesToUserMutation.Variables(
                username = user.username,
                authorities = listOf(authority.value),
            )
        }

        assertEquals(
            authorities + authority.value,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not link an authority to user without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityFactory.create()

        assertGraphQLForbidden {
            client.request(LinkAuthoritiesToUserMutation) {
                authentication = auth.mock()
                variables = LinkAuthoritiesToUserMutation.Variables(
                    username = user.username,
                    authorities = listOf(authority.value),
                )
            }
        }
    }

    @Test
    fun `test should unlink an authority from user`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityFactory.create().also { userAuthorityRepository.link(user, it) }

        val authorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.request(UnlinkAuthoritiesFromUserMutation) {
            authentication = auth.mock("authority.admin", "authority.view")
            variables = UnlinkAuthoritiesFromUserMutation.Variables(
                username = user.username,
                authorities = listOf(authority.value),
            )
        }

        assertEquals(
            authorities - authority.value,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not unlink an authority from user without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityFactory.create().also { userAuthorityRepository.link(user, it) }

        assertGraphQLForbidden {
            client.request(UnlinkAuthoritiesFromUserMutation) {
                authentication = auth.mock()
                variables = UnlinkAuthoritiesFromUserMutation.Variables(
                    username = user.username,
                    authorities = listOf(authority.value),
                )
            }
        }
    }
}
