package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.web.graphql.user.authority.UserAddAuthorityInput
import com.diekeditora.web.graphql.user.authority.UserRemoveAuthorityInput
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLException
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.NOT_ENOUGH_AUTHORITIES
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class UserAuthorityMutationTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val authorityRepository: AuthorityRepository,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val userAuthorityRepository: UserAuthorityRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should add user's an authority`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create())

        val userAuthorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.request(AddAuthorityMutation) {
            authentication = auth.mock("authority.admin")
            variables = AddAuthorityMutation.Variables(
                input = UserAddAuthorityInput(user.username, setOf(authority.value))
            )
        }

        assertEquals(
            userAuthorities + authority,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not add user's an authority without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create())

        assertThrows<GraphQLException>(NOT_ENOUGH_AUTHORITIES) {
            client.request(AddAuthorityMutation) {
                authentication = auth.mock()
                variables = AddAuthorityMutation.Variables(
                    input = UserAddAuthorityInput(user.username, setOf(authority.value))
                )
            }
        }
    }

    @Test
    fun `test should remove an authority from user`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create()).also {
            userAuthorityRepository.save(user, it)
        }

        val userAuthorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.request(RemoveAuthorityMutation) {
            authentication = auth.mock("authority.admin")
            variables = RemoveAuthorityMutation.Variables(
                input = UserRemoveAuthorityInput(user.username, setOf(authority.value))
            )
        }

        assertEquals(
            userAuthorities - authority,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not remove an authority from user without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create()).also {
            userAuthorityRepository.save(user, it)
        }

        assertThrows<GraphQLException>(NOT_ENOUGH_AUTHORITIES) {
            client.request(RemoveAuthorityMutation) {
                authentication = auth.mock()
                variables = RemoveAuthorityMutation.Variables(
                    input = UserRemoveAuthorityInput(user.username, setOf(authority.value))
                )
            }
        }
    }
}
