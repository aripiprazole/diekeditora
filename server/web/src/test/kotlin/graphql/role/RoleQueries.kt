@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.role

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.user.User
import com.diekeditora.web.graphql.role.DeleteRoleInput
import com.diekeditora.web.graphql.role.UpdateRoleInput
import com.diekeditora.web.tests.graphql.TestQuery
import graphql.relay.DefaultConnection
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object RoleQuery : TestQuery<RoleQuery.Variables, Role?>(typeOf<User?>()) {
    private const val name = "\$name"

    override val queryName = "role"
    override val operationName = "RoleQuery"
    override val query = """
        query RoleQuery($name: String!) {
            role(name: $name) {
                name
                authorities
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val name: String)
}

object RolesQuery : TestQuery<RolesQuery.Variables, DefaultConnection<User>>(
    typeOf<DefaultConnection<User>>()
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
                        authorities
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
    override val operationName = "CreateRole"
    override val query = """
        mutation CreateRole($input: Role!) {
            createRole(input: $input) {
                name
                authorities
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: Role)
}

object UpdateRoleMutation : TestQuery<UpdateRoleMutation.Variables, Role?>(typeOf<Role?>()) {
    override val queryName = "updateRole"
    override val operationName = "UpdateRole"
    override val query = """
        mutation UpdateRole($input: UpdateRoleInput!) {
            updateRole(input: $input) {
                name
                authorities
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: UpdateRoleInput)
}

object DeleteRoleMutation : TestQuery<DeleteRoleMutation.Variables, Unit>(typeOf<Unit>()) {
    override val queryName = "deleteRole"
    override val operationName = "DeleteRole"
    override val query = """
        mutation DeleteRole($input: DeleteRoleInput!) {
            deleteRole(input: $input)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: DeleteRoleInput)
}
