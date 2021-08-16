package com.diekeditora.infra.graphql

import com.diekeditora.domain.graphql.Scalar
import com.diekeditora.infra.graphql.directives.SecuredWiring
import com.expediagroup.graphql.generator.directives.KotlinDirectiveWiringFactory
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.Scalars.GraphQLInt
import graphql.relay.Connection
import graphql.relay.Relay
import graphql.schema.Coercing
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLScalarType.newScalar
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference.typeRef
import org.springframework.stereotype.Service
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

@Service
@OptIn(ExperimentalStdlibApi::class)
class DiekEditoraSchemaGeneratorHooks(val scalars: List<Scalar<*, *>>) : SchemaGeneratorHooks {
    override val wiringFactory = KotlinDirectiveWiringFactory(buildMap {
        put("secured", SecuredWiring())
    })

    private val relay = Relay()

    private val cache = mutableMapOf<String, GraphQLType>()
    private val types = mutableMapOf<KType, (KType) -> GraphQLType?>().apply {
        set(typeOf<Unit>(), ::parseUnit)
        set(typeOf<Connection<*>>(), ::parseConnection)

        scalars.forEach { scalar ->
            set(scalar.klass.starProjectedType) {
                newScalar()
                    .name(scalar.name)
                    .description(scalar.description)
                    .coercing(scalar.coercing)
                    .build()
            }
        }
    }

    override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
        val pageInfoType = Relay.pageInfoType.transform {
            it.field(
                newFieldDefinition()
                    .name("totalPages")
                    .type(GraphQLInt)
                    .description("marks the total pages of connection")
                    .build()
            )
        }

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

    private fun parser(type: KType): ((KType) -> GraphQLType?)? {
        return types.entries
            .find { it.key.jvmErasure.qualifiedName == type.jvmErasure.qualifiedName }?.value
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

        val edgeType = edgeType(name, typeRef(name), null, emptyList())

        connectionType(name, edgeType, emptyList())
    }
}
