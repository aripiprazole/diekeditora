package com.diekeditora.web.tests.graphql.role

import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class RoleQueryTests(
    @Autowired private val roleRepository: RoleRepository,
    @Autowired private val roleFactory: RoleFactory,
    @Autowired private val client: GraphQLTestClient,
    @Autowired private val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve paginated roles`(): Unit = runBlocking {
        roleRepository.save(roleFactory.create())

        val pageNumber = 1
        val pageSize = 15
        val items = roleRepository.findPaginated(pageNumber, pageSize).toList()

        val original = SimpleListConnection(items).get(newDataFetchingEnvironment().build())

        val connection = client.request(RolesQuery) {
            authentication = auth.mock("role.view")
            variables = RolesQuery.Variables(pageNumber)
        }

        connection.edges.forEachIndexed { index, edge ->
            val originalEdge = original.edges[index]

            assertEquals(originalEdge.node, edge.node)
            assertEquals(originalEdge.cursor, edge.cursor)
        }

        assertEquals(original.pageInfo.startCursor, connection.pageInfo.startCursor)
        assertEquals(original.pageInfo.endCursor, connection.pageInfo.endCursor)
        assertEquals(original.pageInfo.isHasNextPage, connection.pageInfo.isHasNextPage)
        assertEquals(original.pageInfo.isHasPreviousPage, connection.pageInfo.isHasPreviousPage)
    }

    @Test
    fun `test should not retrieve paginated roles without authorities`(): Unit = runBlocking {
        roleRepository.save(roleFactory.create())

        val pageNumber = 1

        assertGraphQLForbidden {
            client.request(RolesQuery) {
                authentication = auth.mock()
                variables = RolesQuery.Variables(pageNumber)
            }
        }
    }

    @Test
    fun `test should retrieve a role`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())

        val response = client.request(RoleQuery) {
            authentication = auth.mock("role.view")
            variables = RoleQuery.Variables(role.name)
        }.let(::assertNotNull)

        assertEquals(role, response)
    }

    @Test
    fun `test not should retrieve a role without authorities`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())

        assertGraphQLForbidden {
            client.request(RoleQuery) {
                authentication = auth.mock()
                variables = RoleQuery.Variables(role.name)
            }
        }
    }
}
