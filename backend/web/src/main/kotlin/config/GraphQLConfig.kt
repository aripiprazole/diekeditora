package com.lorenzoog.diekeditora.web.config

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

@Configuration
@OptIn(ExperimentalSerializationApi::class)
class GraphQLConfig(val json: Json) {
    @Bean
    fun hooks(): SchemaGeneratorHooks = GenerateKotlinSerializationGraphQLTypesHook()

    @OptIn(ExperimentalStdlibApi::class)
    inner class GenerateKotlinSerializationGraphQLTypesHook : SchemaGeneratorHooks {
        private val scalarCache = mutableMapOf<String, GraphQLScalarType>()

        private val unitScalar = GraphQLScalarType.newScalar()
            .name("Void")
            .coercing(object : Coercing<Unit, Unit> {
                override fun serialize(dataFetcherResult: Any?): Unit = Unit
                override fun parseValue(input: Any?) = Unit
                override fun parseLiteral(input: Any?) = Unit
            })
            .build()

        override fun willGenerateGraphQLType(type: KType): GraphQLType? = runCatching {
            val serializer = json.serializersModule.serializerOrNull(type)
            val descriptor = serializer?.descriptor

            // return default unit scalar
            if (type.jvmErasure == Unit::class) {
                return unitScalar
            }

            // skip builtin serializers
            if (descriptor?.serialName.orEmpty().startsWith("kotlin")) {
                return null
            }

            when (descriptor?.kind) {
                is PrimitiveKind.BOOLEAN -> scalarType(type, String::toBoolean)
                is PrimitiveKind.LONG -> scalarType(type, String::toLong)
                is PrimitiveKind.INT -> scalarType(type, String::toInt)
                is PrimitiveKind.STRING -> scalarType(type) { it }
                is PrimitiveKind.CHAR -> scalarType(type) { it.toCharArray().first() }
                is PrimitiveKind.SHORT -> scalarType(type, String::toShort)
                is PrimitiveKind.BYTE -> scalarType(type, String::toByte)
                is PrimitiveKind.FLOAT -> scalarType(type, String::toFloat)
                is PrimitiveKind.DOUBLE -> scalarType(type, String::toDouble)
                else -> null
            }
        }.getOrNull()

        private fun <T : Any> scalarType(type: KType, transform: (String) -> T): GraphQLType {
            val serializer = requireNotNull(json.serializersModule.serializer(type))
            val name = type.jvmErasure.simpleName.orEmpty()

            // creates a reference if it already exists
            scalarCache[name]?.let {
                return GraphQLTypeReference(name)
            }

            return GraphQLScalarType.newScalar()
                .name(type.jvmErasure.simpleName)
                .coercing(object : Coercing<Any, Any> {
                    override fun serialize(value: Any): Any {
                        return transform(json.encodeToJsonElement(serializer, value).jsonPrimitive.content)
                    }

                    override fun parseValue(input: Any): Any? {
                        return json.decodeFromString(serializer, input.toString())
                    }

                    override fun parseLiteral(input: Any): Any? {
                        return json.decodeFromString(serializer, input.toString())
                    }
                })
                .build()
                .also { scalarCache[name] = it }
        }
    }
}
