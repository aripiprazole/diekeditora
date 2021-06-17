package com.diekeditora.web.tests.graphql.role.authority

import com.diekeditora.infra.repositories.RoleAuthorityRepository
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.utils.AuthenticationMocker
import com.diekeditora.web.tests.utils.assertGraphQLForbidden
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class RoleAuthorityMutationTests(
    @Autowired val roleRepository: RoleRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val roleAuthorityRepository: RoleAuthorityRepository,
    @Autowired val client: GraphQLTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should link an authority to role`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())
        val authority = authorityFactory.create()

        val authorities = roleAuthorityRepository.findByRole(role).toList().map { it.value }

        client.request(LinkAuthoritiesToRoleMutation) {
            authentication = auth.mock("authority.admin", "authority.view")
            variables = LinkAuthoritiesToRoleMutation.Variables(
                name = role.name,
                authorities = listOf(authority.value),
            )
        }

        assertEquals(
            authorities + authority.value,
            roleAuthorityRepository.findByRole(role).toList().map { it.value }
        )
    }

    @Test
    fun `test should not link an authority to role without authorities`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())
        val authority = authorityFactory.create()

        assertGraphQLForbidden {
            client.request(LinkAuthoritiesToRoleMutation) {
                authentication = auth.mock()
                variables = LinkAuthoritiesToRoleMutation.Variables(
                    name = role.name,
                    authorities = listOf(authority.value),
                )
            }
        }
    }

    @Test
    fun `test should unlink an authority from role`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())
        val authority = authorityFactory.create().also { roleAuthorityRepository.link(role, it) }

        val authorities = roleAuthorityRepository.findByRole(role).toList().map { it.value }

        client.request(UnlinkAuthoritiesFromRoleMutation) {
            authentication = auth.mock("authority.admin", "authority.view")
            variables = UnlinkAuthoritiesFromRoleMutation.Variables(
                name = role.name,
                authorities = listOf(authority.value),
            )
        }

        assertEquals(
            authorities - authority.value,
            roleAuthorityRepository.findByRole(role).toList().map { it.value }
        )
    }

    @Test
    fun `test should not unlink an authority from role without authorities`(): Unit = runBlocking {
        val role = roleRepository.save(roleFactory.create())
        val authority = authorityFactory.create().also { roleAuthorityRepository.link(role, it) }

        assertGraphQLForbidden {
            client.request(UnlinkAuthoritiesFromRoleMutation) {
                authentication = auth.mock()
                variables = UnlinkAuthoritiesFromRoleMutation.Variables(
                    name = role.name,
                    authorities = listOf(authority.value),
                )
            }
        }
    }
}
