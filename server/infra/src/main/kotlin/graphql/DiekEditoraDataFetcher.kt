package com.diekeditora.infra.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.exceptions.CouldNotGetNameOfKParameterException
import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

internal class DiekEditoraDataFetcher(
    val target: Any?,
    val fn: KFunction<*>,
    private val objectMapper: ObjectMapper,
) : FunctionDataFetcher(target, fn, objectMapper) {
    override fun get(environment: DataFetchingEnvironment): Any? {
        return runFunction(environment)
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
}
