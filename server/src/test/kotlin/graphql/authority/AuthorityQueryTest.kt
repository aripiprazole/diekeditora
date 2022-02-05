package com.diekeditora.app.tests.graphql.authority

import com.diekeditora.app.tests.factories.AuthorityFactory
import com.diekeditora.app.tests.factories.RoleFactory
import com.diekeditora.app.tests.factories.UserFactory
import com.diekeditora.app.tests.graphql.GraphQLTestClient
import com.diekeditora.authority.domain.AuthorityService
import com.diekeditora.redis.infra.CacheProvider
import com.diekeditora.role.domain.RoleService
import com.diekeditora.user.domain.UserService
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
