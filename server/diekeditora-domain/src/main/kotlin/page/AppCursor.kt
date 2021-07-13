package com.diekeditora.domain.page

import graphql.relay.ConnectionCursor

data class AppCursor(private val value: String) : ConnectionCursor {
    override fun getValue(): String = value
}
