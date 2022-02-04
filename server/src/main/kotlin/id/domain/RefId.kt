package com.diekeditora.id.domain

abstract class RefId<T : Comparable<T>> : UniqueId(), Comparable<RefId<T>> {
    abstract val value: T

    override var rawId: String
        get() = value.toString()
        set(_) = error("Can not set RefId's raw id")

    override fun compareTo(other: RefId<T>): Int = value.compareTo(value)

    override fun toString(): String = when (isNew) {
        true -> this::class.simpleName.toString()
        false -> "${this::class.simpleName}(value=$value}"
    }
}
