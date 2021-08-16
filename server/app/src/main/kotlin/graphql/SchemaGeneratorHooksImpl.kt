package com.diekeditora.app.graphql

import com.diekeditora.infra.graphql.SecuredWiring
import com.expediagroup.graphql.generator.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.Scalars.GraphQLInt
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.Coercing
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
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

@OptIn(ExperimentalStdlibApi::class)
class SchemaGeneratorHooksImpl(val objectMapper: ObjectMapper) : SchemaGeneratorHooks {
    override val wiringFactory = KotlinDirectiveWiringFactory(buildMap {
        put("secured", SecuredWiring())
    })

private val relay = Relay()

    private val pageInfoType = Relay.pageInfoType
        .transform { builder ->
            builder.field(
                newFieldDefinition()
                    .name("totalPages")
                    .type(GraphQLInt)
                    .description("marks the total pages of connection")
                    .build()
            )
        }

    private val types = mutableMapOf<KType, (KType) -> GraphQLType?>()
    private val cache = mutableMapOf<String, GraphQLType>()

    init {
        withSerializer<Unit>(::parseUnit)
        withSerializer<Connection<*>>(::parseConnection)
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

    private fun parseConnection(type: KType): GraphQLObjectType = relay.run {
        val name = type.arguments.firstOrNull()
            ?.type?.jvmErasure?.simpleName
            ?: error("Unable to get name of type arguments of $type")

        connectionType(
            name,
            edgeType(name, typeRef(name), null, emptyList()),
            emptyList()
        )
    }

    override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
        return builder.additionalType(pageInfoType)
    }

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        val name = type.jvmErasure.simpleName.orEmpty().let { typeName ->
            val argumentsString = type.arguments.joinToString("") {
                it.type?.jvmErasure?.simpleName ?: "Any"
            }

            argumentsString + typeName
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

    private inline fun <reified T : Any> withSerializer(noinline parser: (KType) -> GraphQLType?) {
        types[typeOf<T>()] = parser
    }

    private fun parser(type: KType): ((KType) -> GraphQLType?)? {
        return types.entries
            .find { it.key.jvmErasure.qualifiedName == type.jvmErasure.qualifiedName }?.value
    }
}
