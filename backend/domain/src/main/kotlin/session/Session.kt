package com.lorenzoog.diekeditora.domain.session

import com.lorenzoog.diekeditora.domain.connection.Social
import com.lorenzoog.diekeditora.domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val social: Social,
    val token: String,
    val user: User,
)
