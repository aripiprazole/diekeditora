package com.diekeditora.role.infra

import org.intellij.lang.annotations.Language

annotation class PaginationQuery(
    @Language("SQL") val selectQuery: String,
    @Language("SQL") val offsetQuery: String,
)
