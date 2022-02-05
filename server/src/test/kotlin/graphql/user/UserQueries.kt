package com.diekeditora.tests.graphql.user

import com.diekeditora.page.infra.AppPage
import com.diekeditora.tests.graphql.TestQuery
import com.diekeditora.user.domain.User

data class UsersQuery(val first: Int, val after: String? = null) : TestQuery<AppPage<User>>(
    """
    query (${"\$first"}: Int!, ${"\$after"}: String) {
        users(first: ${"\$first"}, after: ${"\$after"}) {
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
                    email
                    username
                    birthday
                    createdAt
                    updatedAt
                    deletedAt
                }
            }
        }
    }
    """.trimIndent()
)

data class UserQuery(val username: String) : TestQuery<User?>(
    """
    query(${"\$username"}: String!) {
        user(username: ${"\$username"}) {
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
