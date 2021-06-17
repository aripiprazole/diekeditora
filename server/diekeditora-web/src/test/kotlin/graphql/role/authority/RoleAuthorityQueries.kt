@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.role.authority

import com.diekeditora.domain.role.RolePayload
import com.diekeditora.web.tests.graphql.TestQuery
import kotlin.reflect.typeOf

object RoleAuthoritiesQuery : TestQuery<RolePayload>(typeOf<RolePayload>()) {
    private const val name = "\$name"

    override val queryName = "role"
    override val content = """
        query RoleAuthoritiesQuery($name: String!) {
            role(name: $name) {
                name
                createdAt
                updatedAt
                authorities
            }
        }
    """.trimIndent()

    data class Variables(val name: String)
}

object LinkAuthoritiesToRoleMutation : TestQuery<RolePayload>(typeOf<RolePayload>()) {
    private const val name = "\$name"
    private const val authorities = "\$authorities"

    override val queryName = "linkAuthoritiesToRole"
    override val content = """
        mutation LinkAuthoritiesToRoleMutation($name: String!, $authorities: [String!]!) {
            linkAuthoritiesToRole(name: $name, authorities: $authorities) {
                name
                createdAt
                updatedAt
                authorities
            }
        }
    """.trimIndent()

    data class Variables(val name: String, val authorities: List<String>)
}

object UnlinkAuthoritiesFromRoleMutation : TestQuery<RolePayload>(typeOf<RolePayload>()) {
    private const val name = "\$name"
    private const val authorities = "\$authorities"

    override val queryName = "unlinkAuthoritiesFromRole"
    override val content = """
        mutation UnlinkAuthoritiesFromRoleMutation($name: String!, $authorities: [String!]!) {
            unlinkAuthoritiesFromRole(name: $name, authorities: $authorities) {
                name
                createdAt
                updatedAt
                authorities
            }
        }
    """.trimIndent()

    data class Variables(val name: String, val authorities: List<String>)
}
