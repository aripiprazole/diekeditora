package com.diekeditora.app.tests.graphql.authority

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.app.tests.graphql.TestQuery

data class AuthoritiesQuery(val first: Int, val after: String? = null) : TestQuery<AppPage<String>>(
    """
    query(${"\$first"}: Int!, ${"\$after"}: String) {
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

data class LinkAuthoritiesToUserMutation(val username: String, val authorities: List<String>) :
    TestQuery<User?>(
        """
        mutation(${"\$username"}: String!, ${"\$authorities"}: [String!]!) {
            linkAuthoritiesToUser(username: ${"\$username"}, authorities: ${"\$authorities"}) {
                name
                email
                username
                birthday
                createdAt
                updatedAt
                deletedAt
            }
        }
        """.trimIndent()
    )

data class UnlinkAuthoritiesFromUserMutation(val username: String, val authorities: List<String>) :
    TestQuery<User?>(
        """
        mutation(${"\$username"}: String!, ${"\$authorities"}: [String!]!) {
            unlinkAuthoritiesFromUser(username: ${"\$username"}, authorities: ${"\$authorities"}) {
                name
                email
                username
                birthday
                createdAt
                updatedAt
                deletedAt
            }
        }
        """.trimIndent()
    )

data class LinkAuthoritiesToRoleMutation(val name: String, val authorities: List<String>) :
    TestQuery<Role?>(
        """
        mutation(${"\$name"}: String!, ${"\$authorities"}: [String!]!) {
            linkAuthoritiesToRole(name: ${"\$name"}, authorities: ${"\$authorities"}) {
                name
                createdAt
                updatedAt
            }
        }
        """.trimIndent()
    )

data class UnlinkAuthoritiesFromRoleMutation(val name: String, val authorities: List<String>) :
    TestQuery<Role?>(
        """
        mutation(${"\$name"}: String!, ${"\$authorities"}: [String!]!) {
            unlinkAuthoritiesFromRole(name: ${"\$name"}, authorities: ${"\$authorities"}) {
                name
                createdAt
                updatedAt
            }
        }
        """.trimIndent()
    )
