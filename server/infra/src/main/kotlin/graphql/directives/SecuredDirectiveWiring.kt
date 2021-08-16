package com.diekeditora.infra.graphql.directives

import com.diekeditora.infra.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.directives.KotlinFieldDirectiveEnvironment
import com.expediagroup.graphql.generator.directives.KotlinSchemaDirectiveEnvironment
import com.expediagroup.graphql.generator.directives.KotlinSchemaDirectiveWiring
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputObjectField
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.util.SimpleMethodInvocation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaMethod

internal class SecuredDirectiveWiring : KotlinSchemaDirectiveWiring {
    private val handler = DefaultMethodSecurityExpressionHandler()

    override fun onField(environment: KotlinFieldDirectiveEnvironment): GraphQLFieldDefinition {
        val dataFetcher = environment.getDataFetcher()

        environment.setDataFetcher { dataFetchingEnvironment ->
            dataFetchingEnvironment
                .getSource<Any>()::class
                .getElementByGraphQLName(environment.element)
                .handleFunction(dataFetchingEnvironment)

            dataFetcher.get(dataFetchingEnvironment)
        }

        return environment.element
    }

    override fun onInputObjectField(
        environment: KotlinSchemaDirectiveEnvironment<GraphQLInputObjectField>,
    ): GraphQLInputObjectField {
        return environment.element
    }

    private fun KClass<*>.getElementByGraphQLName(field: GraphQLFieldDefinition): KFunction<*> {
        return if (field.arguments.size > 0) {
            getPropertyByGraphQLName(field.name).getter
        } else {
            getFunctionByGraphQLName(field.name)
        }
    }

    private fun KClass<*>.getFunctionByGraphQLName(name: String): KFunction<*> {
        val property = declaredMemberFunctions.find {
            it.name == name || it.findAnnotation<GraphQLName>()?.value == name
        }

        return property ?: error("Could not find function ${this.simpleName}.$name")
    }

    private fun KClass<*>.getPropertyByGraphQLName(name: String): KProperty1<out Any, *> {
        val property = declaredMemberProperties.find {
            it.name == name || it.findAnnotation<GraphQLName>()?.value == name
        }

        return property ?: error("Could not find property ${this.simpleName}.$name")
    }

    private fun KFunction<*>.handleFunction(environment: DataFetchingEnvironment) {
        val context = environment.getContext<AuthGraphQLContext>()
        val instance = environment.getSource<Any>()

        findSpELExpressions().forEach { value ->
            val result = parseExpression(value, instance, this, environment, context.authentication)

            if (result == false || result == null) {
                throw AccessDeniedException("Not authenticated")
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
            .getValue(handler.createEvaluationContext(authentication, invocation).apply {
                setVariable("this", instance)
            })
    }
}
