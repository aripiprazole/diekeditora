package com.diekeditora.shared

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

inline fun <reified T : Annotation> KClass<out Any>.findPropertiesByAnnotation(): List<KProperty1<out Any, Any?>> {
    return declaredMemberProperties.filter { it.hasAnnotation<T>() }
}

inline fun <reified T : Annotation, R> KClass<out Any>.findPropertyByAnnotation(): KProperty1<out Any, R>? {
    return declaredMemberProperties
        .filterIsInstance<KProperty1<out Any, R>>()
        .find { it.hasAnnotation<T>() }
}
