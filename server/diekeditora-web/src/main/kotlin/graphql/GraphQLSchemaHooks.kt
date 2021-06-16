package com.diekeditora.web.graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.Coercing
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLScalarType.newScalar
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference.typeRef
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import org.springframework.stereotype.Component
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

typealias TypeConverter = (KType) -> GraphQLType?

@Component
@OptIn(ExperimentalSerializationApi::class, ExperimentalStdlibApi::class)
class GraphQLSchemaHooks(val json: Json) : SchemaGeneratorHooks {
    private val relay = Relay()

    private val types = mutableMapOf<KType, TypeConverter>()
    private val cache = mutableMapOf<String, GraphQLType>()

    init {
        addSerializer<Unit>(::parseUnit)
        addSerializer<Connection<*>>(::parseConnection)
    }

    private fun parseUnit(type: KType): GraphQLType {
        return newScalar()
            .name(type.jvmErasure.simpleName ?: "Unit")
            .coercing(object : Coercing<Unit, Unit> {
                override fun serialize(dataFetcherResult: Any?): Unit = Unit
                override fun parseValue(input: Any?) = Unit
                override fun parseLiteral(input: Any?) = Unit
            })
            .build()
    }

    private fun parseConnection(type: KType): GraphQLObjectType {
        val generic = requireNotNull(type.arguments.first().type)
        val name = generic.jvmErasure.simpleName.orEmpty()
        val edge = relay.edgeType(name, typeRef(name), null, emptyList())

        return relay.connectionType(name, edge, emptyList())
    }

    private fun parseScalar(type: KType, serializer: KSerializer<Any?>?): GraphQLType? {
        val name = serializer?.descriptor?.serialName.orEmpty()

        return when (serializer?.descriptor?.kind) {
            is PrimitiveKind.BOOLEAN -> buildScalar(Boolean.serializer(), name, type)
            is PrimitiveKind.LONG -> buildScalar(Long.serializer(), name, type)
            is PrimitiveKind.INT -> buildScalar(Int.serializer(), name, type)
            is PrimitiveKind.STRING -> buildScalar(String.serializer(), name, type)
            is PrimitiveKind.CHAR -> buildScalar(Char.serializer(), name, type)
            is PrimitiveKind.SHORT -> buildScalar(Short.serializer(), name, type)
            is PrimitiveKind.BYTE -> buildScalar(Byte.serializer(), name, type)
            is PrimitiveKind.FLOAT -> buildScalar(Float.serializer(), name, type)
            is PrimitiveKind.DOUBLE -> buildScalar(Double.serializer(), name, type)
            else -> null
        }
    }

    override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
        return builder.additionalType(Relay.pageInfoType)
    }

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        val name = type.jvmErasure.simpleName.orEmpty().let { typeName ->
            type.arguments.joinToString("") + typeName
        }

        // creates a reference if it already exists
        cache[name]?.let {
            return typeRef(name)
        }

        val parser = findParserOrDefault(type) {
            // skip builtin serializers
            if (type.jvmErasure.qualifiedName.orEmpty().startsWith("kotlin.")) {
                return@findParserOrDefault null
            }

            parseScalar(it, json.serializersModule.serializerOrNull(type))
        }

        return parser(type)?.also {
            cache[name] = it
        }
    }

    private fun <T : Any> buildScalar(
        scalarSerializer: KSerializer<T>,
        name: String,
        type: KType
    ): GraphQLType {
        val serializer = json.serializersModule.serializer(type)

        return newScalar()
            .name(type.jvmErasure.simpleName)
            .coercing(object : Coercing<Any, Any> {
                @Suppress("UNCHECKED_CAST")
                override fun serialize(value: Any): Any {
                    return json.encodeToJsonElement(serializer, value).jsonPrimitive.content
                }

                override fun parseValue(input: Any): Any {
                    if (scalarSerializer.descriptor.kind == PrimitiveKind.STRING) {
                        return json.decodeFromString(scalarSerializer, """"$input"""")
                    }

                    return json.decodeFromString(scalarSerializer, input.toString())
                }

                override fun parseLiteral(input: Any): Any {
                    if (scalarSerializer.descriptor.kind == PrimitiveKind.STRING) {
                        return json.decodeFromString(scalarSerializer, """"$input"""")
                    }

                    return json.decodeFromString(scalarSerializer, input.toString())
                }
            })
            .build()
            .also { cache[name] = it }
    }

    private fun findParserOrDefault(type: KType, default: TypeConverter): TypeConverter {
        return types.entries.find {
            it.key.jvmErasure.qualifiedName == type.jvmErasure.qualifiedName
        }?.value ?: default
    }

    private inline fun <reified T : Any> addSerializer(noinline parser: TypeConverter) {
        types[typeOf<T>()] = parser
    }
}
