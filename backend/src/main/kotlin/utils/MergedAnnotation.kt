package com.lorenzoog.diekeditora.utils

import org.springframework.core.annotation.MergedAnnotation
import org.springframework.core.annotation.MergedAnnotations

inline fun <reified T : Annotation> MergedAnnotations.get(): MergedAnnotation<T> {
    return get(T::class.java)
}

/**
 * Gets value in merged annotation by [name] and [T]
 */
inline fun <reified T> MergedAnnotation<*>.get(name: String): T? {
    return getValue(name, T::class.java).orElse(null)
}
