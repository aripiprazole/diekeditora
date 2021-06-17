package com.diekeditora.web.tests.graphql

import kotlin.reflect.KType

abstract class TestQuery<R>(val responseType: KType) {
    abstract val query: String
    abstract val queryName: String

    open val operationName: String get() = this::class.simpleName ?: "No operation name defined"

    protected val input = "\$input"
}
