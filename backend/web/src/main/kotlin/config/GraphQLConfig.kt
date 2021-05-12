package com.lorenzoog.diekeditora.web.config

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType.newScalar
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference.typeRef
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

@Configuration
@OptIn(ExperimentalSerializationApi::class, ExperimentalStdlibApi::class)
class GraphQLConfig(val json: Json) {
    @Bean
    fun hooks(): SchemaGeneratorHooks = GenerateKotlinSerializationGraphQLTypesHook()

    inner class GenerateKotlinSerializationGraphQLTypesHook : SchemaGeneratorHooks {
        private val relay = Relay()
        private val scalarCache = mutableMapOf<String, GraphQLType>()
        private val unitScalar = newScalar()
            .name("Void")
            .coercing(object : Coercing<Unit, Unit> {
                override fun serialize(dataFetcherResult: Any?): Unit = Unit
                override fun parseValue(input: Any?) = Unit
                override fun parseLiteral(input: Any?) = Unit
            })
            .build()

        override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
            return builder.additionalType(Relay.pageInfoType)
        }

        @Suppress("Detekt.ReturnCount")
        override fun willGenerateGraphQLType(type: KType): GraphQLType? {
            val name = type.jvmErasure.simpleName.orEmpty()

            // connection type
            if (type.isSubtypeOf(typeOf<Connection<*>>())) {
                val genericType = requireNotNull(type.arguments.first().type)
                val typeName = genericType.jvmErasure.simpleName.orEmpty()
                val edgeType = relay.edgeType(typeName, typeRef(typeName), null, emptyList())

                return relay.connectionType(name, edgeType, emptyList())
            }

            // return default unit scalar
            if (type.isSubtypeOf(typeOf<Unit>())) {
                return unitScalar
            }

            val serializer = json.serializersModule.serializerOrNull(type)
            val descriptor = serializer?.descriptor

            // skip builtin serializers
            if (descriptor?.serialName.orEmpty().startsWith("kotlin")) {
                return null
            }

            // creates a reference if it already exists
            scalarCache[name]?.let {
                return typeRef(name)
            }

            return when (descriptor?.kind) {
                is PrimitiveKind.BOOLEAN -> buildScalar(name, type, String::toBoolean)
                is PrimitiveKind.LONG -> buildScalar(name, type, String::toLong)
                is PrimitiveKind.INT -> buildScalar(name, type, String::toInt)
                is PrimitiveKind.STRING -> buildScalar(name, type) { it }
                is PrimitiveKind.CHAR -> buildScalar(name, type) { it.toCharArray().first() }
                is PrimitiveKind.SHORT -> buildScalar(name, type, String::toShort)
                is PrimitiveKind.BYTE -> buildScalar(name, type, String::toByte)
                is PrimitiveKind.FLOAT -> buildScalar(name, type, String::toFloat)
                is PrimitiveKind.DOUBLE -> buildScalar(name, type, String::toDouble)
                else -> null
            }
        }

        private fun <T : Any> buildScalar(
            name: String,
            type: KType,
            transform: (String) -> T
        ): GraphQLType {
            val serializer = requireNotNull(json.serializersModule.serializer(type))

            return newScalar()
                .name(type.jvmErasure.simpleName)
                .coercing(object : Coercing<Any, Any> {
                    override fun serialize(value: Any): Any {
                        return transform(
                            json.encodeToJsonElement(
                                serializer,
                                value
                            ).jsonPrimitive.content
                        )
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
