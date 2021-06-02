package com.diekeditora.shared

inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (ignored: Throwable) {
        null
    }
}
