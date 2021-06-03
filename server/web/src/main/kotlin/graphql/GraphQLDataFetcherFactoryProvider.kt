package com.diekeditora.web.graphql

import com.diekeditora.web.utils.toJsonElement
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.exceptions.CouldNotGetNameOfKParameterException
import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.generator.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.util.SimpleMethodInvocation
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

@Component
class GraphQLDataFetcherFactoryProvider(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val json: Json,
) : KotlinDataFetcherFactoryProvider by SimpleKotlinDataFetcherFactoryProvider() {
    private val handler = DefaultMethodSecurityExpressionHandler()

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) =
        DataFetcherFactory {
            GraphQLDataFetcher(objectMapper, json, target, kFunction, handler)
        }
}

class GraphQLDataFetcher(
    objectMapper: ObjectMapper,
    private val json: Json,
    private val target: Any?,
    private val fn: KFunction<*>,
    private val handler: DefaultMethodSecurityExpressionHandler
) : FunctionDataFetcher(target, fn, objectMapper) {
    private val parser = handler.expressionParser

    override fun get(environment: DataFetchingEnvironment): Any? {
        val expression = fn.findAnnotation<PreAuthorize>()?.value
        val context = environment.getContext<GraphQLContext>() as? AuthGraphQLContext

        if (expression != null && context == null) {
            throw AccessDeniedException("Not authenticated")
        }

        if (expression != null && context != null) {
            context.authenticate(expression, environment)
        }

        return runFunction(environment)
    }

    private fun runFunction(env: DataFetchingEnvironment): Any? {
        val parameters = arrayOf(fn.instanceParameter!! to target) +
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
                    val serializer = json.serializersModule.serializer(param.type)
                    val element = env.getArgument<Any?>(name).toJsonElement()

                    param to json.decodeFromJsonElement(serializer, element)
                } else {
                    null
                }
            }
        }
    }

    private fun AuthGraphQLContext.authenticate(value: String, env: DataFetchingEnvironment) {
        val method = fn.javaMethod ?: error("Could not get java method from function $fn")
        val invocation = SimpleMethodInvocation(target, method, env.arguments.values)

        val result = parser
            .parseExpression(value)
            .getValue(handler.createEvaluationContext(authentication, invocation))

        if (result == false) {
            throw AccessDeniedException("Not enough authorities")
        }
    }
}
