package com.diekeditora.lib

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

fun JsonElement.jsonToAny(): Any? {
  return when (this) {
    is JsonNull -> null
    is JsonArray -> jsonToAny()
    is JsonObject -> jsonToAny()
    is JsonPrimitive -> when (val content = contentOrNull) {
      "true" -> true
      "false" -> false
      null -> null
      else -> when {
        content.toIntOrNull() != null -> content.toInt()
        content.toDoubleOrNull() != null -> content.toDouble()
        content.toFloatOrNull() != null -> content.toFloat()
        content.toLongOrNull() != null -> content.toLong()
        else -> null
      }
    }
  }
}

fun JsonArray.jsonToAny(): List<Any?> {
  return map { it.jsonToAny() }
}

fun JsonObject.jsonToAny(): Map<String, Any?> {
  return entries.associate { (name, element) -> name to element.jsonToAny() }
}
