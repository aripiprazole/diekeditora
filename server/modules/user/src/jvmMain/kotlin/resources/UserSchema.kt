package com.diekeditora.user.resources

import com.diekeditora.lib.ModelSchema
import com.diekeditora.user.domain.User
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.LocalDateTime

@ModelSchema
class UserSchema(@GraphQLIgnore private val user: User) {
  val name: String = user.name
  val email: String = user.email
  val birthday: LocalDateTime = user.birthday
  val createdAt: LocalDateTime = user.createdAt
  val updatedAt: LocalDateTime = user.updatedAt
}
