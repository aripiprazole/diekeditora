package com.diekeditora.web.tests.graphql.user.role

import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserRoleMutationTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val userRoleRepository: UserRoleRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should link a role to user`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create())

        val roles = userRoleRepository.findByUser(user).toList()

        client.request(LinkUserRoleQuery) {
            authentication = auth.mock("role.admin", "role.view")
            variables = LinkUserRoleQuery.Variables(
                username = user.username,
                roles = listOf(role.name)
            )
        }

        assertEquals(roles + role, userRoleRepository.findByUser(user).toList())
    }

    @Test
    fun `test should not link an authority to user without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create())

        assertGraphQLForbidden {
            client.request(LinkUserRoleQuery) {
                authentication = auth.mock()
                variables = LinkUserRoleQuery.Variables(
                    username = user.username,
                    roles = listOf(role.name)
                )
            }
        }
    }

    @Test
    fun `test should unlink a role from user`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create()).also {
            userRoleRepository.link(user, it)
        }

        val roles = userRoleRepository.findByUser(user).toList()

        client.request(UnlinkUserRolesQuery) {
            authentication = auth.mock("role.admin", "role.view")
            variables = UnlinkUserRolesQuery.Variables(
                username = user.username,
                roles = listOf(role.name),
            )
        }

        assertEquals(roles - role, userRoleRepository.findByUser(user).toList())
    }

    @Test
    fun `test should not unlink a role from user without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create()).also {
            userRoleRepository.link(user, it)
        }

        assertGraphQLForbidden {
            client.request(UnlinkUserRolesQuery) {
                authentication = auth.mock()
                variables = UnlinkUserRolesQuery.Variables(
                    username = user.username,
                    roles = listOf(role.name),
                )
            }
        }
    }
}
