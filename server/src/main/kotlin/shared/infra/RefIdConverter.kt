package com.diekeditora.shared.infra

import com.diekeditora.id.domain.RefId
import org.springframework.core.convert.converter.Converter
import kotlin.reflect.KClass

class RefIdConverter<T : Comparable<T>>(private val newIdClass: KClass<out RefId<T>>) :
    Converter<RefId<T>, T> {
    override fun convert(id: RefId<T>): T? {
        return when {
            newIdClass.isInstance(newIdClass) -> null
            else -> id.value
        }
    }
}
