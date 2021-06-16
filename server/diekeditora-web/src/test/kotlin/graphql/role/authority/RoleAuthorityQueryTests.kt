package com.diekeditora.web.tests.graphql.role.authority

import com.diekeditora.infra.repositories.RoleAuthorityRepository
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.request
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class RoleAuthorityQueryTests(
    @Autowired val roleRepository: RoleRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val roleAuthorityRepository: RoleAuthorityRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve role's authorities`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create()).also {
            roleAuthorityRepository.link(it, authorityFactory.createMany(5))
        }

        val response = client.request(RoleAuthoritiesQuery) {
            authentication = auth.mock("role.view", "authority.view")
            variables = RoleAuthoritiesQuery.Variables(name = role.name)
        }

        assertEquals(
            roleAuthorityRepository.findByRole(role).map { it.value }.toList(),
            response.authorities
        )
    }

    @Test
    fun `test should not retrieve role's authorities without authorities`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())

        assertGraphQLForbidden {
            client.request(RoleAuthoritiesQuery) {
                authentication = auth.mock()
                variables = RoleAuthoritiesQuery.Variables(name = role.name)
            }
        }
    }
}
