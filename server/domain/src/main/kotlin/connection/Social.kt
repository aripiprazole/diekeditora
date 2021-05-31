package com.lorenzoog.diekeditora.domain.connection

import kotlinx.serialization.Serializable

@Serializable
data class Social(
    val loggable: Boolean,
    val id: String,
)
