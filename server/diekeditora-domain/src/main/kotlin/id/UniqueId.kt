package com.diekeditora.domain.id

data class UniqueId(val value: String) {
    override fun toString(): String = value
}
