package com.diekeditora.domain.role

import com.diekeditora.domain.validation.NotBlank
import com.diekeditora.domain.validation.Size

@Suppress("Detekt.MagicNumber")
data class RoleInput(@NotBlank @Size(min = 4, max = 32) val name: String) {
    fun toRole(): Role {
        return Role(name = name)
    }

    companion object {
        fun from(role: Role): RoleInput {
            return RoleInput(name = role.name)
        }
    }
}
