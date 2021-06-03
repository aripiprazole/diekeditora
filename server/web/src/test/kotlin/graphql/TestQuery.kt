package com.diekeditora.web.tests.graphql

import kotlin.reflect.KType

abstract class TestQuery<V, R>(val responseType: KType) {
    abstract val query: String
    abstract val queryName: String
    abstract val operationName: String

    protected val input = "\$input"
}
