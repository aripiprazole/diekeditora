package com.lorenzoog.diekeditora.factories

interface Factory<T> {
    fun generateMany(amount: Int = 5): List<T> = (0..amount).map {
        generateEntity()
    }

    fun generateEntity(): T
}
