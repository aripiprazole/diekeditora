package com.diekeditora.web.graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.Coercing
import graphql.schema.GraphQLList
import graphql.schema.GraphQLList.list
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLScalarType.newScalar
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference.typeRef
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

typealias TypeConverter = (KType) -> GraphQLType?

@OptIn(ExperimentalStdlibApi::class)
class GraphQLSchemaHooks(val objectMapper: ObjectMapper) : SchemaGeneratorHooks {
    private val relay = Relay()

    private val types = mutableMapOf<KType, TypeConverter>()
    private val cache = mutableMapOf<String, GraphQLType>()

    init {
        withSerializer<Unit>(::parseUnit)
        withSerializer<Connection<*>>(::parseConnection)
        withSerializer<Set<*>>(::parseSet)
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

    private fun parseSet(type: KType): GraphQLList {
        val generic = requireNotNull(type.arguments.first().type)
        val name = generic.jvmErasure.simpleName.orEmpty()

        return list(typeRef(name))
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

        return parser(type)?.invoke(type)?.also {
            cache[name] = it
        }
    }

    inline fun <reified I, reified O> jacksonCoercing(
        noinline transform: (String) -> O,
    ): Coercing<I, O> = object : Coercing<I, O> {
        override fun serialize(result: Any?): O {
            return transform(objectMapper.valueToTree<ValueNode>(result).asText())
        }

        override fun parseValue(input: Any?): I {
            if (typeOf<O>().isSubtypeOf(typeOf<String>())) {
                return objectMapper.readValue("\"$input\"")
            }

            return objectMapper.readValue(input.toString())
        }

        override fun parseLiteral(input: Any?): I {
            if (typeOf<O>().isSubtypeOf(typeOf<String>())) {
                return objectMapper.readValue("\"$input\"")
            }

            return objectMapper.readValue(input.toString())
        }
    }

    fun withScalar(type: KType, scalar: GraphQLScalarType) {
        types[type] = { scalar }
    }

    inline fun <reified T : Any> withScalar(scalar: GraphQLScalarType) {
        withScalar(typeOf<T>(), scalar)
    }

    private inline fun <reified T : Any> withSerializer(noinline parser: TypeConverter) {
        types[typeOf<T>()] = parser
    }

    private fun parser(type: KType): TypeConverter? {
        return types.entries
            .find { it.key.jvmErasure.qualifiedName == type.jvmErasure.qualifiedName }?.value
    }
}
