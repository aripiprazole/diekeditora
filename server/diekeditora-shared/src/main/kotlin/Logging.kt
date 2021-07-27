package com.diekeditora.shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty

/**
 * Gets a new instance of [Logger] to [T]
 *
 * @param T target logger class
 * @return the new logger
 */
@Suppress("unused")
inline fun <reified T : Any> getLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

fun logger(): ReadOnlyProperty<Any, Logger> = ReadOnlyProperty { thisRef, _ ->
    LoggerFactory.getLogger(thisRef.javaClass)
}
