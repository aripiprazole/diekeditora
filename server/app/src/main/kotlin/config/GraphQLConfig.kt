package com.diekeditora.app.config

import com.expediagroup.graphql.generator.execution.FlowSubscriptionExecutionStrategy
import graphql.execution.ExecutionStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfig {
    @Bean
    fun subscriptionExecutionStrategy(): ExecutionStrategy = FlowSubscriptionExecutionStrategy()
}
