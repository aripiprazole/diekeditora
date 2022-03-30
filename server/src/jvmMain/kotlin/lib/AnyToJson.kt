package com.diekeditora.lib

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.reflect.full.starProjectedType

fun Any?.anyToJson(): JsonElement {
  return when (this) {
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Collection<*> -> anyToJson()
    is Map<*, *> -> anyToJson()
    null -> JsonNull
    else -> error("Unsupported conversion to json element from kotlin type: ${this::class.starProjectedType}")
  }
}

fun Collection<Any?>.anyToJson(): JsonArray {
  return JsonArray(map { it.anyToJson() })
}

fun Map<*, Any?>.anyToJson(): JsonObject {
  return JsonObject(mapKeys { (name) -> name.toString() }.mapValues { it.value.anyToJson() })
}
