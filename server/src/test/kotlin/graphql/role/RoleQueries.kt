package com.diekeditora.tests.graphql.role

import com.diekeditora.page.infra.AppPage
import com.diekeditora.role.domain.Role
import com.diekeditora.tests.graphql.TestQuery

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
