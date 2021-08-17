package com.diekeditora.infra.graphql

import com.expediagroup.graphql.generator.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetcherFactory
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction

@Component
class DiekEditoraDataFetcherFactoryProvider(val objectMapper: ObjectMapper) :
    KotlinDataFetcherFactoryProvider by SimpleKotlinDataFetcherFactoryProvider() {
    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) =
        DataFetcherFactory {
            DiekEditoraDataFetcher(target, kFunction, objectMapper)
        }
}
