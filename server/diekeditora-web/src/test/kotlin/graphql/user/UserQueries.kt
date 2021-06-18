@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.graphql.GraphQLConnection
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserInput
import com.diekeditora.web.tests.graphql.TestQuery
import kotlin.reflect.typeOf

object UserQuery : TestQuery<User>(typeOf<User>()) {
    private const val username = "\$username"

    override val queryName = "user"
    override val content = """
        query UserQuery($username: String!) {
            user(username: $username) {
                name
                username
                email
                birthday
                createdAt
                updatedAt
                deletedAt
            }
        }
    """.trimIndent()

    data class Variables(val username: String)
}

object UsersQuery : TestQuery<GraphQLConnection<User>>(typeOf<GraphQLConnection<User>>()) {
    private const val page = "\$page"

    override val queryName = "users"
    override val content = """
        query UsersQuery($page: Int!) {
            users(page: $page) {
                pageInfo {
                    startCursor
                    endCursor
                    hasNextPage
                    hasPreviousPage
                }
                
                edges {
                    cursor
                    node {
                        name
                        username
                        email
                        birthday
                        createdAt
                        updatedAt
                        deletedAt
                    }
                }
            }
        }
    """.trimIndent()

    data class Variables(val page: Int)
}

object CreateUserMutation : TestQuery<User>(typeOf<User>()) {
    override val queryName = "createUser"
    override val content = """
        mutation CreateUserMutation($input: UserInput!) {
            createUser(input: $input) {
                name
                username
                email
                createdAt
                birthday
                deletedAt
                updatedAt
            }
        }
    """.trimIndent()

    data class Variables(val input: UserInput)
}

object UpdateUserMutation : TestQuery<User?>(typeOf<User?>()) {
    private const val username = "\$username"

    override val queryName = "updateUser"
    override val content = """
        mutation UpdateUserMutation($username: String!, $input: UserInput!) {
            updateUser(username: $username, input: $input) {
                name
                username
                email
                birthday
                createdAt
                deletedAt
                updatedAt
            }
        }
    """.trimIndent()

    data class Variables(val username: String, val input: UserInput)
}

object DeleteUserMutation : TestQuery<User?>(typeOf<User?>()) {
    private const val username = "\$username"

    override val queryName = "deleteUser"
    override val content = """
        mutation DeleteUserMutation($username: String!) {
            deleteUser(username: $username) {
                name
                username
                email
                birthday
                createdAt
                deletedAt
                updatedAt
            }
        }
    """.trimIndent()

    data class Variables(val username: String)
}
