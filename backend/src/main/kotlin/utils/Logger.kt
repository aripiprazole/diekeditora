package com.lorenzoog.diekeditora.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Gets a new instance of [Logger] to [T]
 *
 * @param T target logger class
 * @return the new logger
 */
inline fun <reified T : Any> getLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
