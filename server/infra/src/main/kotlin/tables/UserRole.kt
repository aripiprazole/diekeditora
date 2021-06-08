package com.diekeditora.infra.tables

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table

internal object UserRole : Table("user_role") {
    val role = reference("role_id", Roles, onDelete = CASCADE)
    val user = reference("user_id", Users, onDelete = CASCADE)
}
