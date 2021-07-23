package com.diekeditora.web.tests.graphql.authority

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.page.asNodeList
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.UserService
import com.diekeditora.infra.entities.Authority
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class AuthorityMutationTests(
    @Autowired val userService: UserService,
    @Autowired val userFactory: UserFactory,
    @Autowired val roleService: RoleService,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val authorityService: AuthorityService,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val client: GraphQLTestClient,
) {

    @Test
    fun `test should link authorities to role`(): Unit = runBlocking {
        val first = 15

        val role = roleService.saveRole(roleFactory.create())
        val currentAuthorities = authorityService.findAuthoritiesByRole(role, first)

        val newAuthorities = authorityFactory.createMany(first).map(Authority::value)

        val response = client.request(LinkAuthoritiesToRoleMutation(role.name, newAuthorities)) {
            authenticate("authority.admin")
        }

        assertNotNull(response)
        assertEquals(0, currentAuthorities.edges.size)
        assertTrue(
            newAuthorities.containsAll(
                authorityService.findAuthoritiesByRole(role, first).asNodeList(),
            ),
        )
    }

    @Test
    fun `test should unlink authorities from role`(): Unit = runBlocking {
        val first = 15

        val authorities = authorityFactory.createMany(first).map(Authority::value).toSet()
        val role = roleService.saveRole(roleFactory.create()).also {
            authorityService.linkAuthorities(it, authorities)
        }

        val currentAuthorities = authorityService.findAuthoritiesByRole(role, first)

        val response =
            client.request(UnlinkAuthoritiesFromRoleMutation(role.name, authorities.toList())) {
                authenticate("authority.admin")
            }

        assertNotNull(response)
        assertEquals(first, currentAuthorities.edges.size)
        assertFalse(
            authorityService
                .findAuthoritiesByRole(role, first)
                .asNodeList()
                .containsAll(authorities)
        )
    }

    @Test
    fun `test should link authorities to user`(): Unit = runBlocking {
        val first = 15

        val user = userService.saveUser(userFactory.create())
        val currentAuthorities = authorityService.findAuthoritiesByUser(user, first)

        val newAuthorities = authorityFactory.createMany(first).map(Authority::value)

        val response =
            client.request(LinkAuthoritiesToUserMutation(user.username, newAuthorities)) {
                authenticate("authority.admin")
            }

        assertNotNull(response)
        assertEquals(0, currentAuthorities.edges.size)
        assertTrue(
            newAuthorities.containsAll(
                authorityService.findAuthoritiesByUser(user, first).asNodeList(),
            ),
        )
    }

    @Test
    fun `test should unlink authorities from user`(): Unit = runBlocking {
        val first = 15

        val authorities = authorityFactory.createMany(first).map(Authority::value).toSet()
        val user = userService.saveUser(userFactory.create()).also {
            authorityService.linkAuthorities(it, authorities)
        }

        val currentAuthorities = authorityService.findAuthoritiesByUser(user, first)

        val response =
            client.request(UnlinkAuthoritiesFromUserMutation(user.username, authorities.toList())) {
                authenticate("authority.admin")
            }

        assertNotNull(response)
        assertEquals(first, currentAuthorities.edges.size)
        assertFalse(
            authorityService
                .findAuthoritiesByUser(user, first)
                .asNodeList()
                .containsAll(authorities)
        )
    }
}