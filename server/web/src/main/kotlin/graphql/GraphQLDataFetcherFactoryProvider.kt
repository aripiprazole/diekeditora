package com.diekeditora.web.graphql

import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.generator.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.util.SimpleMethodInvocation
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaMethod

@Component
class GraphQLDataFetcherFactoryProvider(
    private val delegate: KotlinDataFetcherFactoryProvider = SimpleKotlinDataFetcherFactoryProvider()
) : KotlinDataFetcherFactoryProvider by delegate {
    private val handler = DefaultMethodSecurityExpressionHandler()

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) =
        DataFetcherFactory {
            GraphQLDataFetcher(target, kFunction, handler)
        }
}

class GraphQLDataFetcher(
    private val target: Any?,
    private val fn: KFunction<*>,
    private val handler: DefaultMethodSecurityExpressionHandler
) : FunctionDataFetcher(target, fn) {
    private val parser = handler.expressionParser

    override fun get(environment: DataFetchingEnvironment): Any? {
        val expression = fn.findAnnotation<PreAuthorize>()?.value
        val context = environment.getContext<GraphQLContext>() as? AuthGraphQLContext

        if (expression != null && context == null) {
            throw AccessDeniedException("Not authenticated")
        }

        if (expression != null && context != null) {
            context.runChecks(expression, environment)
        }

        return super.get(environment)
    }

    private fun AuthGraphQLContext.runChecks(value: String, env: DataFetchingEnvironment) {
        val method = fn.javaMethod ?: error("Could not get java method from function $fn")
        val invocation = SimpleMethodInvocation(target, method, env.arguments.values)

        val result = parser
            .parseExpression(value)
            .getValue(handler.createEvaluationContext(authentication, invocation))

        if (result == false) {
            throw AccessDeniedException("Not enough permissions")
        }
    }
}
