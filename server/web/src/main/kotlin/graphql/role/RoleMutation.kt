package com.diekeditora.web.graphql.role

import com.diekeditora.domain.authority.Role
import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoleInput(val name: String)

@Serializable
data class UpdateRoleInput(val name: String, val role: Role)
