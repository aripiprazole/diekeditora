package com.diekeditora.infra.adapters

import com.diekeditora.domain.authority.Role
import com.diekeditora.infra.tables.Roles
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

internal suspend fun mapRole(row: ResultRow): Role = newSuspendedTransaction {
    Role(
        id = row[Roles.id].value
    )
}
