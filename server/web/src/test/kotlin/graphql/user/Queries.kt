@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user

import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserInput
import com.diekeditora.web.graphql.user.CreateUserPayload
import com.diekeditora.web.graphql.user.DeleteUserInput
import com.diekeditora.web.graphql.user.UpdateUserInput
import com.diekeditora.web.graphql.user.UpdateUserPayload
import com.diekeditora.web.tests.graphql.TestQuery
import graphql.relay.DefaultConnection
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserQuery : TestQuery<UserQuery.Variables, User>(typeOf<User>()) {
    private const val username = "\$username"

    override val queryName = "user"
    override val operationName = "UserQuery"
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

object UsersQuery : TestQuery<UsersQuery.Variables, DefaultConnection<User>>(
    typeOf<DefaultConnection<User>>()
) {
    private const val page = "\$page"

    override val queryName = "users"
    override val operationName = "UsersQuery"
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

object CreateUserMutation : TestQuery<CreateUserMutation.Variables, CreateUserPayload>(
    typeOf<CreateUserPayload>()
) {
    override val queryName = "createUser"
    override val operationName = "CreateUser"
    override val query = """
        mutation CreateUser($input: UserInput!) {
            createUser(input: $input) {
                user {
                    name
                    username
                    email
                    createdAt
                    birthday
                    deletedAt
                    updatedAt
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: UserInput)
}

object UpdateUserMutation : TestQuery<UpdateUserMutation.Variables, UpdateUserPayload>(
    typeOf<UpdateUserPayload>()
) {
    override val queryName = "updateUser"
    override val operationName = "UpdateUser"
    override val query = """
        mutation UpdateUser($input: UpdateUserInput!) {
            updateUser(input: $input) {
                user {
                    name
                    username
                    email
                    birthday
                    createdAt
                    deletedAt
                    updatedAt
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: UpdateUserInput)
}

object DeleteUserMutation : TestQuery<DeleteUserMutation.Variables, UpdateUserPayload>(
    typeOf<UpdateUserPayload>()
) {
    override val queryName = "deleteUser"
    override val operationName = "DeleteUser"
    override val query = """
        mutation DeleteUser($input: DeleteUserInput!) {
            deleteUser(input: $input) {
                user {
                    name
                    username
                    email
                    birthday
                    createdAt
                    deletedAt
                    updatedAt
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: DeleteUserInput)
}
