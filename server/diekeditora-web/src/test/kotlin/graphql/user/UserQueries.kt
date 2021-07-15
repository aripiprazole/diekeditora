package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.user.User
import com.diekeditora.web.tests.graphql.TestQuery

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
            profile
            createdAt
            updatedAt
            deletedAt
        }
    }
    """.trimIndent()
)
