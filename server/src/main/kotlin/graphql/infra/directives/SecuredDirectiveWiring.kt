package com.diekeditora.graphql.infra.directives

import com.diekeditora.graphql.infra.AuthGraphQLContext
import com.diekeditora.graphql.infra.DiekEditoraDataFetcher
import com.expediagroup.graphql.generator.directives.KotlinFieldDirectiveEnvironment
import com.expediagroup.graphql.generator.directives.KotlinSchemaDirectiveWiring
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.util.SimpleMethodInvocation
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

internal class SecuredDirectiveWiring : KotlinSchemaDirectiveWiring {
    private val handler = DefaultMethodSecurityExpressionHandler()

    override fun onField(environment: KotlinFieldDirectiveEnvironment): GraphQLFieldDefinition {
        val dataFetcher = environment.getDataFetcher() as DiekEditoraDataFetcher

        environment.setDataFetcher { dataFetchingEnvironment ->
            dataFetcher.fn.handleFunction(dataFetcher.target, dataFetchingEnvironment)

            dataFetcher.get(dataFetchingEnvironment)
        }

        return environment.element
    }

    private fun KFunction<*>.handleFunction(instance: Any?, environment: DataFetchingEnvironment) {
        val context = environment.getContext<AuthGraphQLContext>()
        val target = environment.getSource<Any>()
            ?: instance ?: error("Target is null, can not proceed")

        findSpELExpressions().forEach { value ->
            val result = parseExpression(value, target, this, environment, context.authentication)

            if (result == false || result == null) {
                throw AccessDeniedException("Not authorized")
            }
        }
    }

    private fun KFunction<*>.findSpELExpressions(): List<String> {
        return annotations
            .flatMap { it.annotationClass.annotations + it }
            .filterIsInstance<PreAuthorize>()
            .map { it.value }
    }

    private fun parseExpression(
        expression: String,
        instance: Any,
        fn: KFunction<*>,
        environment: DataFetchingEnvironment,
        authentication: Authentication,
    ): Any? {
        val method = fn.javaMethod ?: error("Could not get java method from function $fn")
        val invocation = SimpleMethodInvocation(instance, method, environment.arguments.values)

        return handler.expressionParser
            .parseExpression(expression)
            .getValue(
                handler.createEvaluationContext(authentication, invocation).apply {
                    setVariable("this", instance)
                }
            )
    }
}
