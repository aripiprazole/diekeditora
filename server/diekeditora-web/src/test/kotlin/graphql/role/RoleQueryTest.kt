package com.diekeditora.web.tests.graphql.role

import com.diekeditora.domain.role.RoleService
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class RoleQueryTest(
    @Autowired val roleService: RoleService,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val client: GraphQLTestClient,
) {

    @Test
    fun `test should retrieve all roles paginated`(): Unit = runBlocking {
        val first = 15
        val roles = roleFactory.createMany(first * 2)
            .forEach { role -> roleService.saveRole(role) }
            .let { roleService.findRoles(first) }

        val response = client.request(RolesQuery(first)) {
            authenticate("role.view")
        }

        assertEquals(roles, response)
    }

    @Test
    fun `test should retrieve role by name`(): Unit = runBlocking {
        val role = roleFactory.create()

        val response = client.request(RoleQuery(role.name)) {
            authenticate("role.view")
        }

        assertEquals(role, response)
    }
}
