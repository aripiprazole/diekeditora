package com.diekeditora.id.domain

fun interface UniqueIdService {
    fun generateUniqueId(): UniqueId
}
