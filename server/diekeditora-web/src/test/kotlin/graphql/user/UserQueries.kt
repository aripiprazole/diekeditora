@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserInput
import com.diekeditora.web.tests.graphql.TestQuery
import graphql.relay.DefaultConnection
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserQuery : TestQuery<User>(typeOf<User>()) {
    private const val username = "\$username"

    override val queryName = "user"
    override val query = """
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

    @Serializable
    data class Variables(val username: String)
}

object UsersQuery : TestQuery<DefaultConnection<User>>(typeOf<DefaultConnection<User>>()) {
    private const val page = "\$page"

    override val queryName = "users"
    override val query = """
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

    @Serializable
    data class Variables(val page: Int)
}

object CreateUserMutation : TestQuery<User>(typeOf<User>()) {
    override val queryName = "createUser"
    override val query = """
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

    @Serializable
    data class Variables(val input: UserInput)
}

object UpdateUserMutation : TestQuery<User?>(typeOf<User?>()) {
    private const val username = "\$username"

    override val queryName = "updateUser"
    override val query = """
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

    @Serializable
    data class Variables(val username: String, val input: UserInput)
}

object DeleteUserMutation : TestQuery<User?>(typeOf<User?>()) {
    private const val username = "\$username"

    override val queryName = "deleteUser"
    override val query = """
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

    @Serializable
    data class Variables(val username: String)
}
