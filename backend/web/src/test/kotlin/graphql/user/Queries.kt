@file:OptIn(ExperimentalStdlibApi::class)

package com.lorenzoog.diekeditora.web.tests.graphql.user

import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.domain.user.UserInput
import com.lorenzoog.diekeditora.web.graphql.user.DeleteUserInput
import com.lorenzoog.diekeditora.web.graphql.user.UpdateUserInput
import com.lorenzoog.diekeditora.web.graphql.user.UpdateUserPayload
import com.lorenzoog.diekeditora.web.tests.graphql.TestQuery
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
                emailVerifiedAt
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
                        emailVerifiedAt
                        deletedAt
                    }
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val page: Int)
}

object CreateUserMutation : TestQuery<CreateUserMutation.Variables, UpdateUserPayload>(
    typeOf<UpdateUserPayload>()
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
                    emailVerifiedAt
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
                    createdAt
                    emailVerifiedAt
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
            deleteUser(input: $input)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: DeleteUserInput)
}
