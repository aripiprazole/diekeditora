package com.diekeditora.domain.id

fun interface UniqueIdService {
    fun generateUniqueId(): UniqueId
}
