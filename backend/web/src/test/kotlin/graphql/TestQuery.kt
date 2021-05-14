package com.lorenzoog.diekeditora.web.graphql

import kotlin.reflect.KType

abstract class TestQuery<V, R : Any>(val responseType: KType) {
    abstract val query: String
    abstract val operationName: String

    protected val input = "\$input"
}
