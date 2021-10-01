package com.diekeditora.id.domain

import java.util.UUID

open class UniqueId() {
    open lateinit var rawId: String
        protected set

    val isNew: Boolean get() = runCatching { rawId }.isFailure

    constructor(rawId: String) : this() {
        this.rawId = rawId
    }

    operator fun component0(): String = rawId

    fun copy(value: String = this.rawId): UniqueId {
        return UniqueId(value)
    }

    fun toUUID(): UUID = UUID.fromString(rawId)

    override fun toString(): String {
        return rawId
    }
}

