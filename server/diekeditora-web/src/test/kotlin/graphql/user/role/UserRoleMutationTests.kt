package com.diekeditora.web.tests.graphql.user.role

import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import com.diekeditora.web.graphql.user.role.UserAddRoleInput
import com.diekeditora.web.graphql.user.role.UserRemoveRoleInput
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
    @Autowired val userFactory: UserFactory,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val userRoleRepository: UserRoleRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should add user's a role`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create())

        val userRoles = userRoleRepository.findByUser(user).toList()

        client.request(AddRoleMutation) {
            authentication = auth.mock("role.admin")
            variables = AddRoleMutation.Variables(
                input = UserAddRoleInput(user.username, setOf(role.name))
            )
        }

        assertEquals(userRoles + role, userRoleRepository.findByUser(user).toList())
    }

    @Test
    fun `test should not add user's a role without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create())

        assertGraphQLForbidden {
            client.request(AddRoleMutation) {
                authentication = auth.mock()
                variables = AddRoleMutation.Variables(
                    input = UserAddRoleInput(user.username, setOf(role.name))
                )
            }
        }
    }

    @Test
    fun `test should remove a role from user`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create()).also {
            userRoleRepository.save(user, it)
        }

        val userRoles = userRoleRepository.findByUser(user).toList()

        client.request(RemoveRoleMutation) {
            authentication = auth.mock("role.admin")
            variables = RemoveRoleMutation.Variables(
                input = UserRemoveRoleInput(user.username, setOf(role.name))
            )
        }

        assertEquals(userRoles - role, userRoleRepository.findByUser(user).toList())
    }

    @Test
    fun `test should not remove a role from user without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create()).also {
            userRoleRepository.save(user, it)
        }

        assertGraphQLForbidden {
            client.request(RemoveRoleMutation) {
                authentication = auth.mock()
                variables = RemoveRoleMutation.Variables(
                    input = UserRemoveRoleInput(user.username, setOf(role.name))
                )
            }
        }
    }
}