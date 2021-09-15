package com.diekeditora.id.domain

import java.util.UUID

data class UniqueId(val value: String) {
    fun toUUID(): UUID = UUID.fromString(value)

    override fun toString(): String = value
}
