package com.diekeditora.web.tests.graphql.authority

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.UserService
import com.diekeditora.infra.redis.CacheProvider
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import graphql.relay.Connection
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class AuthorityQueryTest(
    @Autowired val authorityService: AuthorityService,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val userService: UserService,
    @Autowired val userFactory: UserFactory,
    @Autowired val roleService: RoleService,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val cacheProvider: CacheProvider,
    @Autowired val client: GraphQLTestClient,
) {
    @Test
    fun `test should retrieve all authorities paginated`(): Unit = runBlocking {
        val user = userService.saveUser(userFactory.create())
        val role = roleService.saveRole(roleFactory.create())

        val first = 15
        val after = null
        val authorities = authorityFactory.createMany(first * 2)
            .toSet()
            .also { authorities -> authorityService.linkAuthorities(user, authorities) }
            .also { authorities -> authorityService.linkAuthorities(role, authorities) }
            .let { authorityService.findAllAuthorities(first) }

        cacheProvider
            .repo<Connection<String>>()
            .delete("authorityConnection.$first.$after")

        val response = client.request(AuthoritiesQuery(first)) {
            authenticate("authority.view")
        }

        assertEquals(authorities, response)
    }
}
