package com.diekeditora.id.domain

import kotlin.reflect.KProperty

class IdNotPersistedDelegate<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        error("Entity not persisted yet")
    }
}
