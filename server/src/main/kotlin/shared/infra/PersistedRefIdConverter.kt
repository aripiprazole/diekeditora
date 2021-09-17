package com.diekeditora.shared.infra

import com.diekeditora.id.domain.RefId
import org.springframework.core.convert.converter.Converter

class PersistedRefIdConverter<T : Comparable<T>>(val persistedConstructor: (T) -> RefId<T>) :
    Converter<T, RefId<T>> {
    override fun convert(target: T): RefId<T> {
        return persistedConstructor(target)
    }
}
