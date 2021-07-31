package com.diekeditora.app.tests.graphql.role

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.role.Role
import com.diekeditora.app.tests.graphql.TestQuery

data class RolesQuery(val first: Int, val after: String? = null) : TestQuery<AppPage<Role>>(
    """
    query (${"\$first"}: Int!, ${"\$after"}: String) {
        roles(first: ${"\$first"}, after: ${"\$after"}) {
            pageInfo {
                hasNextPage
                hasPreviousPage
                startCursor
                endCursor
                totalPages
            }
            
            edges {
                cursor
                node {
                    name
                    createdAt
                    updatedAt
                }
            }
        }
    }
    """.trimIndent()
)

data class RoleQuery(val name: String) : TestQuery<Role?>(
    """
    query(${"\$name"}: String!) {
        role(name: ${"\$name"}) {
            name
            createdAt
            updatedAt
        }
    }
    """.trimIndent()
)
