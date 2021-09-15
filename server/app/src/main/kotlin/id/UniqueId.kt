package com.diekeditora.app.id

import java.util.UUID

data class UniqueId(val value: String) {
    fun toUUID(): UUID = UUID.fromString(value)

    override fun toString(): String = value
}
