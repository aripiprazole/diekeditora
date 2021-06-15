@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserAuthoritiesQuery : TestQuery<UserAuthoritiesQuery.Variables, UserResponse>(
    typeOf<UserResponse>()
) {
    private const val username = "\$username"
    override val queryName = "user"
    override val operationName = "UserAuthoritiesQuery"
    override val query = """
        query UserAuthoritiesQuery($username: String!) {
            user(username: $username) {
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String)
}

object UpdateUserAuthoritiesQuery : TestQuery<UpdateUserAuthoritiesQuery.Variables, UserResponse>(
    typeOf<UserResponse>()
) {
    private const val username = "\$username"
    private const val authorities = "\$authorities"

    override val queryName = "update"
    override val operationName = "UpdateUserAuthoritiesQuery"
    override val query = """
        mutation UpdateUserAuthoritiesQuery($username: String!, $authorities: [String!]) {
            updateUser(username: $username, authorities: $authorities) {
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String, val authorities: List<String>)
}

@Serializable
data class UserResponse(val authorities: List<String>)
