package com.diekeditora.infra.entities

class Authority(val authority: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (authority != other.authority) return false

        return true
    }

    override fun hashCode(): Int {
        return authority.hashCode()
    }

    override fun toString(): String {
        return "Authority($authority)"
    }

    companion object {
        fun of(value: String): Authority {
            return Authority(value)
        }
    }
}
