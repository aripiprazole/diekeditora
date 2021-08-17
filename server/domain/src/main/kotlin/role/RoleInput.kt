package com.diekeditora.domain.role

@Suppress("Detekt.MagicNumber")
data class RoleInput(val name: String) {
    fun toRole(): Role {
        return Role(name = name)
    }

    companion object {
        fun from(role: Role): RoleInput {
            return RoleInput(name = role.name)
        }
    }
}
