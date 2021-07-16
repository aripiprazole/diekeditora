package com.diekeditora.web.tests.graphql.authority

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.UserService
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.graphql.GraphQLTestClient
import com.diekeditora.web.tests.graphql.TestQuery
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
    @Autowired val client: GraphQLTestClient,
) {
    @Test
    fun `test should retrieve all authorities paginated`(): Unit = runBlocking {
        val user = userService.saveUser(userFactory.create())
        val role = roleService.saveRole(roleFactory.create())

        val first = 15
        val authorities = authorityFactory.createMany(first * 2)
            .map { it.value }
            .also { authorities -> authorityService.linkAuthorities(user, authorities.toSet()) }
            .also { authorities -> authorityService.linkAuthorities(role, authorities.toSet()) }
            .let { authorityService.findAllAuthorities(first) }

        val response = client.request(AuthoritiesQuery(first)) {
            authenticate("authority.view")
        }

        assertEquals(authorities, response)
    }
}

data class AuthoritiesQuery(val first: Int, val after: String? = null) : TestQuery<AppPage<String>>(
    """
    query(${"\$first"}: Int!, ${"\$after"}: String!) {
        authorities(first: ${"\$first"}, after: ${"\$after"}) {
            pageInfo {
                hasNextPage
                hasPreviousPage
                startCursor
                endCursor
                totalPages
            }
            
            edges {
                cursor
                node
            }
        }
    }
    """.trimIndent()
)
