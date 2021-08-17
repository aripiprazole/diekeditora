package com.diekeditora.domain.role

import org.valiktor.functions.hasSize
import org.valiktor.validate

@Suppress("Detekt.MagicNumber")
data class RoleInput(val name: String) {
    init {
        validate(this) {
            validate(RoleInput::name).hasSize(min = 4, max = 32)
        }
    }

    fun toRole(): Role {
        return Role(name = name)
    }

    companion object {
        fun from(role: Role): RoleInput {
            return RoleInput(name = role.name)
        }
    }
}
