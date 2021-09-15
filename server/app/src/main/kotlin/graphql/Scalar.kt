package com.diekeditora.app.graphql

import graphql.schema.Coercing
import kotlin.reflect.KClass

interface Scalar<I : Any, O> {
    val klass: KClass<I>
    val name: String get() = klass.simpleName ?: error("Could not get klass name")
    val description: String get() = ""
    val coercing: Coercing<I, O>
}
