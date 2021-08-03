package com.diekeditora.app.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.exceptions.CouldNotGetNameOfKParameterException
import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.util.SimpleMethodInvocation
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

class FunctionDataFetcherImpl(
    private val objectMapper: ObjectMapper,
    private val target: Any?,
    private val fn: KFunction<*>,
    private val handler: DefaultMethodSecurityExpressionHandler
) : FunctionDataFetcher(target, fn, objectMapper) {
    private val parser = handler.expressionParser

    override fun get(environment: DataFetchingEnvironment): Any? {
        val expression = fn.findAnnotation<PreAuthorize>()?.value
        val secured = fn.findAnnotation<Secured>()?.value
        val context = environment.getContext<GraphQLContext>() as? AuthGraphQLContext

        return runFunction(environment)

        when {
            expression != null && context == null -> throw AccessDeniedException("Not authenticated")
            expression != null && context != null -> context.preAuthorize(expression, environment)
            secured != null && context == null -> throw AccessDeniedException("Not authenticated")
            secured != null && context != null -> context.secured(secured.toList())
        }
    }

    private fun runFunction(env: DataFetchingEnvironment): Any? {
        val instance: Any? = target ?: env.getSource()

        val parameters = arrayOf(fn.instanceParameter!! to instance) +
            fn.valueParameters.mapNotNull { mapParameter(it, env) }

        return if (fn.isSuspend) {
            runSuspendingFunction(parameters.toMap())
        } else {
            runBlockingFunction(parameters.toMap())
        }
    }

    private fun mapParameter(
        param: KParameter,
        env: DataFetchingEnvironment
    ): Pair<KParameter, Any?>? {
        return when {
            param.type.jvmErasure.isSubclassOf(GraphQLContext::class) -> param to env.getContext()
            param.type.jvmErasure.isSubclassOf(DataFetchingEnvironment::class) -> param to env
            else -> {
                val name =
                    param.findAnnotation<GraphQLName>()?.value
                        ?: param.name
                        ?: throw CouldNotGetNameOfKParameterException(param)

                if (env.containsArgument(name) || param.type.jvmErasure.isSubclassOf(OptionalInput::class)) {
                    // rewrite value to be used in real scenario
                    val value = env.getArgument<Any?>(name)

                    when {
                        param.type.isMarkedNullable && value == null -> param to null
                        param.type.jvmErasure.isInstance(value) -> param to value
                        else -> {
                            param to objectMapper.convertValue(value, param.type.jvmErasure.java)
                        }
                    }
                } else {
                    null
                }
            }
        }
    }

    private fun AuthGraphQLContext.secured(values: List<String>) {
        if (!authentication.authorities.map { it.authority }.containsAll(values)) {
            throw AccessDeniedException("Not enough authorities")
        }
    }

    private fun AuthGraphQLContext.preAuthorize(value: String, env: DataFetchingEnvironment) {
        val instance = target ?: env.getSource()

        val method = fn.javaMethod ?: error("Could not get java method from function $fn")
        val invocation = SimpleMethodInvocation(instance, method, env.arguments.values)

        val result = parser
            .parseExpression(value)
            .getValue(handler.createEvaluationContext(authentication, invocation))

        if (result == false) {
            throw AccessDeniedException("Not enough authorities")
        }
    }
}
