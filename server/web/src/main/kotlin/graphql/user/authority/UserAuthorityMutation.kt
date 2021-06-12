package com.diekeditora.web.graphql.user.authority

import com.expediagroup.graphql.server.operations.Mutation
import kotlinx.serialization.Serializable
import org.springframework.stereotype.Component

@Component
class UserAuthorityMutation : Mutation

@Serializable
class UserAddAuthorityInput(val username: String, val authorities: Set<String>)

@Serializable
class UserRemoveAuthorityInput(val username: String, val authorities: Set<String>)
