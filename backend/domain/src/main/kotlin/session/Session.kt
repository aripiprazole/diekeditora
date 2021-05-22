package com.lorenzoog.diekeditora.domain.session

import com.lorenzoog.diekeditora.domain.user.User

data class Session(
    val user: User,
    val token: String
)
