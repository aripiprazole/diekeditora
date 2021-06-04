package com.diekeditora.web.graphql.user.role

import com.expediagroup.graphql.server.operations.Mutation
import kotlinx.serialization.Serializable
import org.springframework.stereotype.Component

@Component
class UserRoleMutation : Mutation

@Serializable
class UserAddRoleInput(val username: String, val roles: Set<String>)
