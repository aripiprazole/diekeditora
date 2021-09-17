package com.diekeditora.config

import com.expediagroup.graphql.generator.execution.FlowSubscriptionExecutionStrategy
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.ExecutionStrategy
import graphql.infra.GraphQLExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfig {
    @Bean
    fun dataFetcherExceptionHandler(): DataFetcherExceptionHandler =
        GraphQLExceptionHandler()

    @Bean
    fun subscriptionExecutionStrategy(exceptionHandler: DataFetcherExceptionHandler): ExecutionStrategy =
        FlowSubscriptionExecutionStrategy(exceptionHandler)
}
