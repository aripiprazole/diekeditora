@file:OptIn(ExperimentalStdlibApi::class)

package com.lorenzoog.diekeditora.web.graphql.user

import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.domain.user.UserCreateDto
import com.lorenzoog.diekeditora.web.graphql.TestQuery
import graphql.relay.DefaultConnection
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserQuery : TestQuery<UserQuery.Variables, User>(typeOf<User>()) {
    private const val username = "\$username"

    override val operationName = "UserQuery"

    override val query = """
        mutation UserQuery($username: String) {
            name
            username
            email
            birthday
            createdAt
            updatedAt
            emailVerifiedAt
            deletedAt
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String)
}

object UsersQuery : TestQuery<UsersQuery.Variables, DefaultConnection<User>>(
    typeOf<DefaultConnection<User>>()
) {
    private const val page = "\$page"

    override val operationName = "UsersQuery"

    override val query = """
        mutation UsersQuery($page: Int) {
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

object CreateUserMutation : TestQuery<UserCreateDto, UpdateUserPayload>(
    typeOf<UpdateUserPayload>()
) {
    override val operationName = "CreateUser"

    override val query = """
        query CreateUser($input: CreateUserInput) {
            createUser(input: $input) {
                name,
                username,
                email,
                createdAt,
                emailVerifiedAt,
                deletedAt,
                updatedAt,
            }
        }
    """.trimIndent()
}

object UpdateUserMutation : TestQuery<UpdateUserInput, UpdateUserPayload>(
    typeOf<UpdateUserPayload>()
) {
    override val operationName = "UpdateUser"

    override val query = """
        query UpdateUser($input: UpdateUserInput) {
            updateUser(input: $input) {
                name,
                username,
                email,
                createdAt,
                emailVerifiedAt,
                deletedAt,
                updatedAt,
            }
        }
    """.trimIndent()
}

object DeleteUserMutation : TestQuery<UpdateUserInput, UpdateUserPayload>(
    typeOf<UpdateUserPayload>()
) {
    override val operationName = "UpdateUser"

    override val query = """
        query UpdateUser($input: UpdateUserInput) {
            updateUser(input: $input) {
                name,
                username,
                email,
                createdAt,
                emailVerifiedAt,
                deletedAt,
                updatedAt,
            }
        }
    """.trimIndent()
}
