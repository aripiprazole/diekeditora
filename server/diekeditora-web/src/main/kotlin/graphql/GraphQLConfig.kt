package com.diekeditora.web.graphql

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.image.Upload
import com.expediagroup.graphql.generator.execution.FlowSubscriptionExecutionStrategy
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.execution.ExecutionStrategy
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType.newScalar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.multipart.FilePart
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Configuration
class GraphQLConfig(val objectMapper: ObjectMapper) {
    @Bean
    fun subscriptionExecutionStrategy(): ExecutionStrategy = FlowSubscriptionExecutionStrategy()

    @Bean
    @OptIn(ExperimentalStdlibApi::class)
    fun hooks(): SchemaGeneratorHooks = GraphQLSchemaHooks(objectMapper).apply {
        withScalar<Upload>(
            newScalar()
                .name("Upload")
                .coercing(object : Coercing<Upload, Unit> {
                    override fun serialize(dataFetcherResult: Any?) {
                        error("Could not parse literal Upload")
                    }

                    override fun parseValue(input: Any?): Upload {
                        val part = input as? FilePart
                            ?: error("Could not parse a non-file part object in Upload scalar")

                        return Upload(part)
                    }

                    override fun parseLiteral(input: Any?): Upload {
                        error("Could not parse literal Upload")
                    }
                })
                .build()
        )

        withScalar<UniqueId>(
            newScalar()
                .name("UniqueId")
                .coercing(jacksonCoercing<UniqueId, String> { it })
                .build()
        )

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
