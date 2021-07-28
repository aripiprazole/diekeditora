package com.diekeditora.app.graphql

import com.expediagroup.graphql.generator.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetcherFactory
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction

@Component
class KotlinDataFetcherFactoryProviderImpl(
    val objectMapper: ObjectMapper
) : KotlinDataFetcherFactoryProvider by SimpleKotlinDataFetcherFactoryProvider() {
    private val handler = DefaultMethodSecurityExpressionHandler()

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) =
        DataFetcherFactory {
            FunctionDataFetcherImpl(objectMapper, target, kFunction, handler)
        }
}
