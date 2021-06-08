package com.diekeditora.infra.tables

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table

internal object UserAuthority : Table("user_authority") {
    val user = reference("user_id", Users, onDelete = CASCADE)
    val authority = varchar("value", 80)
}
