@file:OptIn(ExperimentalStdlibApi::class)

package com.diekeditora.web.tests.graphql.user.authority

import com.diekeditora.web.graphql.user.authority.UserAddAuthorityInput
import com.diekeditora.web.tests.graphql.TestQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object UserAuthoritiesQuery : TestQuery<UserAuthoritiesQuery.Variables, Flow<String>>(
    typeOf<Flow<String>>()
) {
    private const val username = "\$username"
    override val queryName = "userAuthorities"
    override val operationName = "UserAuthoritiesQuery"
    override val query = """
        mutation UserAuthoritiesQuery($username: String!) {
            userAuthorities(username: $username)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val username: String)
}

object AddAuthorityMutation : TestQuery<AddAuthorityMutation.Variables, Unit>(typeOf<Unit>()) {
    override val queryName = "addAuthority"
    override val operationName = "AddAuthority"
    override val query = """
        mutation AddRole($input: UserAddAuthorityInput!) {
            addAuthority(input: $input)
        }
    """.trimIndent()

    @Serializable
    data class Variables(val input: UserAddAuthorityInput)
}
