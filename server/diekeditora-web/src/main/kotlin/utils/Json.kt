package com.diekeditora.web.utils

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.reflect.full.starProjectedType

fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        is Map<*, *> -> toJsonElement()
        is List<*> -> toJsonElement()
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        null -> JsonNull
        else -> error("Invalid object ${this::class.starProjectedType}")
    }
}

fun List<*>.toJsonElement(): JsonElement {
    return JsonArray(map { it.toJsonElement() })
}

fun Map<*, *>.toJsonElement(): JsonElement {
    return JsonObject(mapKeys { it.key as String }.mapValues { it.value.toJsonElement() })
}
