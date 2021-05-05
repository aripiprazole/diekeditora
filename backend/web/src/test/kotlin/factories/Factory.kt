package com.lorenzoog.diekeditora.web.factories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Factory<T> {
    fun createMany(amount: Int = 5): Flow<T> = flow {
        repeat(amount) { emit(create()) }
    }

    fun create(): T
}
