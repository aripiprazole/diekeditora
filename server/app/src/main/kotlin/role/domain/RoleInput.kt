package com.diekeditora.app.role.domain

import org.valiktor.functions.hasSize
import org.valiktor.validate

@Suppress("Detekt.MagicNumber")
data class RoleInput(val name: String) {
    fun toRole(): Role = validate(Role(name = name)) {
        validate(Role::name).hasSize(min = 4, max = 32)
    }

    companion object {
        fun from(role: Role): RoleInput {
            return RoleInput(name = role.name)
        }
    }
}
