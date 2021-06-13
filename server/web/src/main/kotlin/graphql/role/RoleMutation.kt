package com.diekeditora.web.graphql.role

import com.diekeditora.domain.authority.Role
import com.expediagroup.graphql.server.operations.Mutation
import kotlinx.serialization.Serializable
import org.springframework.stereotype.Component

@Component
class RoleMutation : Mutation

@Serializable
data class DeleteRoleInput(val name: String)

@Serializable
data class UpdateRoleInput(val name: String, val role: Role)
