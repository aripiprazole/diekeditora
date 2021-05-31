package com.diekeditora.domain.session

import com.diekeditora.domain.connection.Social
import com.diekeditora.domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val social: Social,
    val token: String,
    val user: User,
)
