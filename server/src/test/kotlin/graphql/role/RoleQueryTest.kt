package com.diekeditora.app.tests.graphql.role

import com.diekeditora.app.tests.factories.RoleFactory
import com.diekeditora.app.tests.graphql.GraphQLTestClient
import com.diekeditora.redis.infra.CacheProvider
import com.diekeditora.role.domain.Role
import com.diekeditora.role.domain.RoleService
import graphql.relay.Connection
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class RoleQueryTest(
    @Autowired val roleService: RoleService,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val cacheProvider: CacheProvider,
    @Autowired val client: GraphQLTestClient,
) {

    @Test
    fun `test should retrieve all roles paginated`(): Unit = runBlocking {
        val first = 15
        val after = null
        val roles = roleFactory.createMany(first * 2)
            .forEach { role -> roleService.saveRole(role) }
            .let { roleService.findRoles(first) }

        cacheProvider
            .repo<Connection<Role>>()
            .delete("roleConnection.$first.$after")

        val response = client.request(RolesQuery(first)) {
            authenticate("role.view")
        }

        assertEquals(roles, response)
    }

    @Test
    fun `test should retrieve role by name`(): Unit = runBlocking {
        val role = roleService.saveRole(roleFactory.create())

        val response = client.request(RoleQuery(role.name)) {
            authenticate("role.view")
        }

        assertEquals(role, response)
    }
}
