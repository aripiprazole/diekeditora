package com.diekeditora.infra.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime

internal object Users : UUIDTable("user") {
    val name = varchar("name", 60)
    val username = varchar("name", 16)
    val email = varchar("email", 80)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").nullable()
    val deletedAt = datetime("updated_at").nullable()
}
