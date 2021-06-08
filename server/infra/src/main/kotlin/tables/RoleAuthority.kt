package com.diekeditora.infra.tables

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table

internal object RoleAuthority : Table("role_authority") {
    val role = reference("role_id", Roles, onDelete = CASCADE)
    val authority = varchar("value", 80)
}
