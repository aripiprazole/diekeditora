package com.diekeditora.app.tests.graphql.authority

import com.diekeditora.app.tests.factories.AuthorityFactory
import com.diekeditora.app.tests.factories.RoleFactory
import com.diekeditora.app.tests.factories.UserFactory
import com.diekeditora.app.tests.graphql.GraphQLTestClient
import com.diekeditora.authority.domain.AuthorityService
import com.diekeditora.page.infra.asNodeList
import com.diekeditora.redis.infra.CacheProvider
import com.diekeditora.role.domain.RoleService
import com.diekeditora.user.domain.UserService
import graphql.relay.Connection
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
    @Autowired val cacheProvider: CacheProvider,
    @Autowired val client: GraphQLTestClient,
) {

    @Test
    fun `test should link authorities to role`(): Unit = runBlocking {
        val first = 15
        val after = null

        val role = roleService.saveRole(roleFactory.create())
        val currentAuthorities = authorityService.findAuthoritiesByRole(role, first)

        val newAuthorities = authorityFactory.createMany(first).toList()

        val response = client.request(LinkAuthoritiesToRoleMutation(role.name, newAuthorities)) {
            authenticate("authority.admin")
        }

        cacheProvider
            .repo<Connection<String>>()
            .delete("roleAuthority.${role.cursor}.$first.$after")

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
        val after = null

        val authorities = authorityFactory.createMany(first).toSet()
        val role = roleService.saveRole(roleFactory.create()).also {
            authorityService.linkAuthorities(it, authorities)
        }

        val currentAuthorities = authorityService.findAuthoritiesByRole(role, first)

        val response =
            client.request(UnlinkAuthoritiesFromRoleMutation(role.name, authorities.toList())) {
                authenticate("authority.admin")
            }

        cacheProvider
            .repo<Connection<String>>()
            .delete("roleAuthority.${role.cursor}.$first.$after")

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
        val after = null

        val user = userService.saveUser(userFactory.create())
        val currentAuthorities = authorityService.findAuthoritiesByUser(user, first)

        val newAuthorities = authorityFactory.createMany(first).toList()

        val response =
            client.request(LinkAuthoritiesToUserMutation(user.username, newAuthorities)) {
                authenticate("authority.admin")
            }

        cacheProvider
            .repo<Connection<String>>()
            .delete("userAuthority.${user.cursor}.$first.$after")

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
        val after = null

        val authorities = authorityFactory.createMany(first).toSet()
        val user = userService.saveUser(userFactory.create()).also {
            authorityService.linkAuthorities(it, authorities)
        }

        val currentAuthorities = authorityService.findAuthoritiesByUser(user, first)

        val response =
            client.request(UnlinkAuthoritiesFromUserMutation(user.username, authorities.toList())) {
                authenticate("authority.admin")
            }

        cacheProvider
            .repo<Connection<String>>()
            .delete("userAuthority.${user.cursor}.$first.$after")

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
