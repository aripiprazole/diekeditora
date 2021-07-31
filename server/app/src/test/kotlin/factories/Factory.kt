package com.diekeditora.app.tests.factories

interface Factory<T> {
    @OptIn(ExperimentalStdlibApi::class)
    fun createMany(amount: Int = 5): Set<T> = buildSet {
        repeat(amount) { add(create()) }
    }

    fun create(): T
}
