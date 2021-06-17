@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.role

import com.diekeditora.domain.role.Role
import com.diekeditora.web.tests.graphql.TestQuery
import graphql.relay.DefaultConnection
import kotlin.reflect.typeOf

object RoleQuery : TestQuery<Role>(typeOf<Role>()) {
    private const val name = "\$name"

    override val queryName = "role"
    override val content = """
        query RoleQuery($name: String!) {
            role(name: $name) {
                name
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    data class Variables(val name: String)
}

object RolesQuery : TestQuery<DefaultConnection<Role>>(typeOf<DefaultConnection<Role>>()) {
    private const val page = "\$page"

    override val queryName = "roles"
    override val content = """
        query RolesQuery($page: Int!) {
            roles(page: $page) {
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
                        createdAt
                        updatedAt
                    }
                }
            }
        }
    """.trimIndent()

    data class Variables(val page: Int)
}

object CreateRoleMutation : TestQuery<Role>(typeOf<Role>()) {
    override val queryName = "createRole"
    override val content = """
        mutation CreateRoleMutation($input: RoleInput!) {
            createRole(input: $input) {
                name
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    data class Variables(val input: Role)
}

object UpdateRoleMutation : TestQuery<Role>(typeOf<Role>()) {
    private const val name = "\$name"

    override val queryName = "updateRole"
    override val content = """
        mutation UpdateRoleMutation($name: String!, $input: RoleInput!) {
            updateRole(name: $name, input: $input) {
                name
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    data class Variables(val name: String, val input: Role)
}

object DeleteRoleMutation : TestQuery<Unit>(typeOf<Unit>()) {
    private const val name = "\$name"

    override val queryName = "deleteRole"
    override val content = """
        mutation DeleteRoleMutation($name: String!) {
            deleteRole(name: $name)
        }
    """.trimIndent()

    data class Variables(val name: String)
}
