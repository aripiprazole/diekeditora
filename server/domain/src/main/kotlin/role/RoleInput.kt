package com.diekeditora.domain.role

import com.diekeditora.domain.graphql.Max
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank

@Suppress("Detekt.MagicNumber")
data class RoleInput(@NotBlank @Max(32) @Min(4) val name: String) {
    fun toRole(): Role {
        return Role(name = name)
    }

    companion object {
        fun from(role: Role): RoleInput {
            return RoleInput(name = role.name)
        }
    }
}
