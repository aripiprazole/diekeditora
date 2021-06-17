@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.role.authority

import com.diekeditora.domain.role.RolePayload
import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object RoleAuthoritiesQuery : TestQuery<RoleAuthoritiesQuery.Variables, RolePayload>(
    typeOf<RolePayload>()
) {
    private const val name = "\$name"

    override val queryName = "role"
    override val operationName = "RoleAuthoritiesQuery"
    override val query = """
        query RoleAuthoritiesQuery($name: String!) {
            role(name: $name) {
                name
                createdAt
                updatedAt
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String)
}

object LinkAuthoritiesToRoleMutation : TestQuery<LinkAuthoritiesToRoleMutation.Variables, RolePayload>(
    typeOf<RolePayload>()
) {
    private const val name = "\$name"
    private const val authorities = "\$authorities"

    override val queryName = "linkAuthoritiesToRole"
    override val operationName = "LinkAuthoritiesToRoleMutation"
    override val query = """
        mutation LinkAuthoritiesToRoleMutation($name: String!, $authorities: [String!]!) {
            linkAuthoritiesToRole(name: $name, authorities: $authorities) {
                name
                createdAt
                updatedAt
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String, val authorities: List<String>)
}

object UnlinkAuthoritiesFromRoleMutation : TestQuery<UnlinkAuthoritiesFromRoleMutation.Variables, RolePayload>(
    typeOf<RolePayload>()
) {
    private const val name = "\$name"
    private const val authorities = "\$authorities"

    override val queryName = "unlinkAuthoritiesFromRole"
    override val operationName = "UnlinkAuthoritiesFromRoleMutation"
    override val query = """
        mutation UnlinkAuthoritiesFromRoleMutation($name: String!, $authorities: [String!]!) {
            unlinkAuthoritiesFromRole(name: $name, authorities: $authorities) {
                name
                createdAt
                updatedAt
                authorities
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String, val authorities: List<String>)
}
