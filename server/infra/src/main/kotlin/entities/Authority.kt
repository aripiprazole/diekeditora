package com.diekeditora.infra.entities

import org.springframework.data.annotation.Id
import java.util.UUID

class Authority(@Id val id: UUID? = null, val value: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "Authority($value)"
    }

    companion object {
        fun of(value: String): Authority {
            return Authority(null, value)
        }
    }
}
