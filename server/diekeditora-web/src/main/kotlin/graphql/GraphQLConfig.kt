package com.diekeditora.web.graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.GraphQLScalarType.newScalar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Configuration
class GraphQLConfig(val objectMapper: ObjectMapper) {
    @Bean
    fun hooks(): SchemaGeneratorHooks = GraphQLSchemaHooks(objectMapper).apply {
        withScalar<Instant>(
            newScalar()
                .name("Instant")
                .coercing(jacksonCoercing<Instant, Long> { it.toLong() })
                .build()
        )

        withScalar<Date>(
            newScalar()
                .name("Date")
                .coercing(jacksonCoercing<Date, Long> { it.toLong() })
                .build()
        )

        withScalar<LocalDate>(
            newScalar()
                .name("LocalDate")
                .coercing(jacksonCoercing<LocalDate, String> { it })
                .build()
        )

        withScalar<LocalDateTime>(
            newScalar()
                .name("LocalDateTime")
                .coercing(jacksonCoercing<LocalDateTime, String> { it })
                .build()
        )
    }
}
