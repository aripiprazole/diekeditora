package com.lorenzoog.diekeditora.utils

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.types.ScalarDSL
import com.lorenzoog.diekeditora.graphql.GraphQLExtension
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

typealias Scalar<T, R> = (kClass: KClass<T>, block: ScalarDSL<T, R>.() -> Unit) -> Unit

fun SchemaBuilder.with(extension: GraphQLExtension) {
    extension.run { setup() }
}

@OptIn(ExperimentalSerializationApi::class, ExperimentalStdlibApi::class)
inline fun <reified T : Any> SchemaBuilder.serializable(
    format: StringFormat = Json,
    serializer: KSerializer<T> = format.serializersModule.serializer()
) {
    val transform = getTransformer(serializer)
    val createScalar = getScalar(serializer)

    createScalar(T::class) {
        serialize = { transform(format.encodeToString(serializer, it)) }
        deserialize = { format.decodeFromString(serializer, it.toString()) }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun <T : Any> getTransformer(serializer: KSerializer<T>): (String) -> Any {
    return when (serializer.descriptor.kind) {
        is PrimitiveKind.LONG -> fun(string) = string.toLong()
        is PrimitiveKind.STRING -> fun(string) = string
        is PrimitiveKind.INT -> fun(string) = string.toInt()
        is PrimitiveKind.FLOAT -> fun(string) = string.toFloat()
        is PrimitiveKind.BOOLEAN -> fun(string) = string.toBoolean()
        else -> throw IllegalArgumentException("Serial kind for serializer could not bind to graphql $serializer")
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun <T : Any> SchemaBuilder.getScalar(serializer: KSerializer<T>): Scalar<T, Any> {
    val scalar: Scalar<T, *> = when (serializer.descriptor.kind) {
        is PrimitiveKind.LONG -> ::longScalar
        is PrimitiveKind.STRING -> ::stringScalar
        is PrimitiveKind.INT -> ::intScalar
        is PrimitiveKind.FLOAT -> ::floatScalar
        is PrimitiveKind.BOOLEAN -> ::booleanScalar
        else -> throw IllegalArgumentException("Serial kind for serializer could not bind to graphql $serializer")
    }

    @Suppress("UNCHECKED_CAST")
    return scalar as Scalar<T, Any>
}
