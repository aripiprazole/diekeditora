package com.diekeditora.graphql.infra.utils

import com.diekeditora.graphql.domain.Scalar
import com.fasterxml.jackson.databind.ObjectMapper

internal class ScalarBuilder(private val objectMapper: ObjectMapper) {
    private var description = ""

    fun description(description: String) {
        this.description = description
    }

    inline fun <reified I : Any> deserializeString(): Scalar<I, String> =
        object : Scalar<I, String> {
            override val klass = I::class
            override val description = this@ScalarBuilder.description
            override val coercing = objectMapper.coercing<I, String> { it }
        }

    inline fun <reified I : Any, reified O> deserialize(noinline fn: (String) -> O): Scalar<I, O> =
        object : Scalar<I, O> {
            override val klass = I::class
            override val description = this@ScalarBuilder.description
            override val coercing = objectMapper.coercing<I, O>(fn)
        }
}

internal fun jacksonScalar(objectMapper: ObjectMapper): ScalarBuilder = ScalarBuilder(objectMapper)
