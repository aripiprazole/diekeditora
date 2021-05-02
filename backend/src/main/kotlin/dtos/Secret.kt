package com.lorenzoog.diekeditora.dtos

import com.lorenzoog.diekeditora.serializers.SecretSerializer
import kotlinx.serialization.Serializable

@Serializable(with = SecretSerializer::class)
sealed class Secret {
    class Value(val value: String) : Secret() {
        override fun toString(): String = "Secret.Value"
    }

    object None : Secret() {
        override fun toString(): String = "Secret.None"
    }

    companion object {
        fun of(value: String): Secret = Value(value)
    }

    fun unwrap(): String = when (this) {
        is Value -> value
        is None -> throw IllegalArgumentException("Trying to unwrap a None secret")
    }

    fun orNull(): String? = when (this) {
        is Value -> value
        is None -> null
    }

    override fun equals(other: Any?): Boolean = true
    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String = "Secret"
}

fun String?.asSecret(): Secret {
    return Secret.of(this ?: return Secret.None)
}
