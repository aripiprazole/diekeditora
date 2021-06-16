@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user.role

import com.diekeditora.domain.role.Role
import com.diekeditora.web.graphql.user.role.UserAddRoleInput
import com.diekeditora.web.graphql.user.role.UserRemoveRoleInput
import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserRolesQuery : TestQuery<UserRolesQuery.Variables, Flow<Role>>(typeOf<Flow<Role>>()) {
    private const val username = "\$username"
    override val queryName = "userRoles"
    override val operationName = "UserRolesQuery"
    override val query = """
        mutation UserRolesQuery($username: String!) {
            userRoles(username: $username) {
                name
                authorities
                createdAt
                updatedAt
            }
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String)
}

object AddRoleMutation : TestQuery<AddRoleMutation.Variables, Unit>(typeOf<Unit>()) {
    override val queryName = "addRole"
    override val operationName = "AddRole"
    override val query = """
        mutation AddRole($input: UserAddRoleInput!) {
            addRole(input: $input)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: UserAddRoleInput)
}

object RemoveRoleMutation : TestQuery<RemoveRoleMutation.Variables, Unit>(typeOf<Unit>()) {
    override val queryName = "removeRoles"
    override val operationName = "RemoveRoles"
    override val query = """
        mutation RemoveRoles($input: UserRemoveRoleInput!) {
            removeRoles(input: $input)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: UserRemoveRoleInput)
}
