package com.diekeditora.page.domain

import org.intellij.lang.annotations.Language

annotation class PaginationQuery(
    @Language("SQL") val selectQuery: String,
    @Language("SQL") val offsetQuery: String,
)
