package com.diekeditora.web.tests.graphql.user.role

import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLException
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.NOT_ENOUGH_AUTHORITIES
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserRoleQueryTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val userRoleRepository: UserRoleRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve user's roles`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userRoleRepository.save(it, roleRepository.saveAll(roleFactory.createMany(5)).toList())
        }

        val response = client.request(UserRolesQuery) {
            authentication = auth.mock("role.view")
            variables = UserRolesQuery.Variables(username = user.username)
        }

        assertEquals(userRoleRepository.findByUser(user), response)
    }

    @Test
    fun `test should not retrieve user's roles without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        assertThrows<GraphQLException>(NOT_ENOUGH_AUTHORITIES) {
            client.request(UserRolesQuery) {
                authentication = auth.mock()
                variables = UserRolesQuery.Variables(username = user.username)
            }
        }
    }
}
