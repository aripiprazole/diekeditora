@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.domain.user.UserPayload
import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserAuthoritiesQuery : TestQuery<UserAuthoritiesQuery.Variables, UserPayload>(
    typeOf<UserPayload>()
) {
    private const val username = "\$username"
    override val queryName = "user"
    override val operationName = "UserAuthoritiesQuery"
    override val query = """
        query UserAuthoritiesQuery($username: String!) {
            user(username: $username) {
                name
                username
                email
                birthday
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String)
}

object LinkUserAuthoritiesQuery : TestQuery<LinkUserAuthoritiesQuery.Variables, UserPayload>(
    typeOf<UserPayload>()
) {
    private const val username = "\$username"
    private const val authorities = "\$authorities"

    override val queryName = "linkAuthoritiesToUser"
    override val operationName = "LinkUserAuthoritiesQuery"
    override val query = """
        mutation LinkUserAuthoritiesQuery($username: String!, $authorities: [String!]!) {
            linkAuthoritiesToUser(username: $username, authorities: $authorities) {
                name
                username
                email
                birthday
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String, val authorities: List<String>)
}

object UnlinkUserAuthoritiesQuery : TestQuery<UnlinkUserAuthoritiesQuery.Variables, UserPayload>(
    typeOf<UserPayload>()
) {
    private const val username = "\$username"
    private const val authorities = "\$authorities"

    override val queryName = "unlinkAuthoritiesFromUser"
    override val operationName = "UnlinkUserAuthoritiesQuery"
    override val query = """
        mutation UnlinkUserAuthoritiesQuery($username: String!, $authorities: [String!]!) {
            unlinkAuthoritiesFromUser(username: $username, authorities: $authorities) {
                name
                username
                email
                birthday
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String, val authorities: List<String>)
}
