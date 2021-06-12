package com.diekeditora.web.tests.graphql.role

import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.web.graphql.role.DeleteRoleInput
import com.diekeditora.web.graphql.role.UpdateRoleInput
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class RoleMutationTests(
    @Autowired private val roleRepository: RoleRepository,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val roleFactory: RoleFactory,
    @Autowired private val auth: AuthenticationMocker,
) {

    @Test
    fun `test should create role`(): Unit = runBlocking {
        var role = roleFactory.create()

        val name = role.name
        val authorities = role.authorities

        role = client.request(CreateRoleMutation) {
            authentication = auth.mock("role.store")
            variables = CreateRoleMutation.Variables(role)
        }

        assertEquals(name, role.name)
        assertEquals(authorities, role.authorities)
    }

    @Test
    fun `test should not create role without authorities`(): Unit = runBlocking {
        val role = roleFactory.create()

        assertGraphQLForbidden {
            client.request(CreateRoleMutation) {
                authentication = auth.mock()
                variables = CreateRoleMutation.Variables(role)
            }
        }
    }

    @Test
    fun `test should update role by name`(): Unit = runBlocking {
        var role = roleFactory.create().let { roleRepository.save(it) }
        val newRole = roleFactory.create()

        role = client.request(UpdateRoleMutation) {
            authentication = auth.mock("role.update")
            variables = UpdateRoleMutation.Variables(
                input = UpdateRoleInput(role.name, newRole),
            )
        }.let(::assertNotNull)

        assertEquals(newRole.name, role.name)
        assertNotNull(role.updatedAt)
    }

    @Test
    fun `test not should update role by name without authorities`(): Unit = runBlocking {
        val role = roleFactory.create().let { roleRepository.save(it) }
        val newRole = roleFactory.create()

        assertGraphQLForbidden {
            client.request(UpdateRoleMutation) {
                authentication = auth.mock()
                variables = UpdateRoleMutation.Variables(
                    input = UpdateRoleInput(role.name, newRole),
                )
            }
        }
    }

    @Test
    fun `test should delete role by name`(): Unit = runBlocking {
        val role = roleFactory.create().let { roleRepository.save(it) }

        client.request(DeleteRoleMutation) {
            authentication = auth.mock("role.destroy")
            variables = DeleteRoleMutation.Variables(input = DeleteRoleInput(role.name))
        }

        assertNull(roleRepository.findById(role.id!!))
    }

    @Test
    fun `test should not delete role by name without authorities`(): Unit = runBlocking {
        val role = roleFactory.create().let { roleRepository.save(it) }

        assertGraphQLForbidden {
            client.request(DeleteRoleMutation) {
                authentication = auth.mock()
                variables = DeleteRoleMutation.Variables(input = DeleteRoleInput(role.name))
            }
        }
    }
}
