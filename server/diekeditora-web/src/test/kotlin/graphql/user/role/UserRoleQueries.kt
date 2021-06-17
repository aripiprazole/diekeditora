@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user.role

import com.diekeditora.domain.user.UserPayload
import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserRolesQuery : TestQuery<UserPayload>(typeOf<UserPayload>()) {
    private const val username = "\$username"

    override val queryName = "user"
    override val query = """
        query UserRolesQuery($username: String!) {
            user(username: $username) {
                name
                username
                email
                roles {
                    name
                    updatedAt
                    createdAt
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String)
}

object LinkRolesToUserMutation : TestQuery<UserPayload>(typeOf<UserPayload>()) {
    private const val username = "\$username"
    private const val roles = "\$roles"

    override val queryName = "linkRolesToUser"
    override val query = """
        mutation LinkUserRolesMutation($username: String!, $roles: [String!]!) {
            linkRolesToUser(username: $username, roles: $roles) {
                name
                username
                email
                roles {
                    name
                    updatedAt
                    createdAt
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String, val roles: List<String>)
}

object UnlinkRolesFromUserMutation : TestQuery<UserPayload>(typeOf<UserPayload>()) {
    private const val username = "\$username"
    private const val roles = "\$roles"

    override val queryName = "unlinkRolesFromUser"
    override val query = """
        mutation UnlinkRolesFromUserMutation($username: String!, $roles: [String!]!) {
            unlinkRolesFromUser(username: $username, roles: $roles) {
                name
                username
                email
                roles {
                    name
                    updatedAt
                    createdAt
                }
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String, val roles: List<String>)
}
