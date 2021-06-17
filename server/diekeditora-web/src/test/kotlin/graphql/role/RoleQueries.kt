@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.role

import com.diekeditora.domain.role.Role
import com.diekeditora.web.tests.graphql.TestQuery
import graphql.relay.DefaultConnection
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object RoleQuery : TestQuery<RoleQuery.Variables, Role?>(typeOf<Role?>()) {
    private const val name = "\$name"

    override val queryName = "role"
    override val operationName = "RoleQuery"
    override val query = """
        query RoleQuery($name: String!) {
            role(name: $name) {
                name
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String)
}

object RolesQuery : TestQuery<RolesQuery.Variables, DefaultConnection<Role>>(
    typeOf<DefaultConnection<Role>>()
) {
    private const val page = "\$page"

    override val queryName = "roles"
    override val operationName = "RolesQuery"
    override val query = """
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

    @Serializable
    data class Variables(val page: Int)
}

object CreateRoleMutation : TestQuery<CreateRoleMutation.Variables, Role>(typeOf<Role>()) {
    override val queryName = "createRole"
    override val operationName = "CreateRoleMutation"
    override val query = """
        mutation CreateRoleMutation($input: RoleInput!) {
            createRole(input: $input) {
                name
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: Role)
}

object UpdateRoleMutation : TestQuery<UpdateRoleMutation.Variables, Role?>(typeOf<Role?>()) {
    private const val name = "\$name"
    override val queryName = "updateRole"
    override val operationName = "UpdateRoleMutation"
    override val query = """
        mutation UpdateRoleMutation($name: String!, $input: RoleInput!) {
            updateRole(name: $name, input: $input) {
                name
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String, val input: Role)
}

object DeleteRoleMutation : TestQuery<DeleteRoleMutation.Variables, Unit>(typeOf<Unit>()) {
    private const val name = "\$name"

    override val queryName = "deleteRole"
    override val operationName = "DeleteRoleMutation"
    override val query = """
        mutation DeleteRoleMutation($name: String!) {
            deleteRole(name: $name)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String)
}
