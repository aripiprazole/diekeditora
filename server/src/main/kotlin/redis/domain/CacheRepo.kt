package com.diekeditora.redis.domain

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface CacheRepo<T : Any> {
    suspend fun remember(key: String, expiresIn: Duration, cacheFn: suspend () -> T): T

    suspend fun query(key: String, expiresIn: Duration, cacheFn: suspend () -> T?): T?

    suspend fun delete(key: String)
}
