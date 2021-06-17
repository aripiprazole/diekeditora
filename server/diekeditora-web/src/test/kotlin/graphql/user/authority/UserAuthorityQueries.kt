@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.domain.user.UserPayload
import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserAuthoritiesQuery : TestQuery<UserPayload>(typeOf<UserPayload>()) {
    private const val username = "\$username"

    override val queryName = "user"
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

object LinkAuthoritiesToUserMutation : TestQuery<UserPayload>(typeOf<UserPayload>()) {
    private const val username = "\$username"
    private const val authorities = "\$authorities"

    override val queryName = "linkAuthoritiesToUser"
    override val query = """
        mutation LinkAuthoritiesToUserMutation($username: String!, $authorities: [String!]!) {
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

object UnlinkAuthoritiesFromUserMutation : TestQuery<UserPayload>(typeOf<UserPayload>()) {
    private const val username = "\$username"
    private const val authorities = "\$authorities"

    override val queryName = "unlinkAuthoritiesFromUser"
    override val query = """
        mutation UnlinkAuthoritiesFromUserMutation($username: String!, $authorities: [String!]!) {
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
