package com.diekeditora.infra.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.deleteAndAwait
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.hasKeyAndAwait
import org.springframework.data.redis.core.setAndAwait
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@OptIn(ExperimentalTime::class)
interface CacheRepo<T : Any> {
    suspend fun remember(key: String, expiresIn: Duration, cacheFn: suspend () -> T): T

    suspend fun query(key: String, expiresIn: Duration, cacheFn: suspend () -> T?): T?

    suspend fun delete(key: String)
}

@OptIn(ExperimentalTime::class)
@PublishedApi
internal class CacheRepoImpl<T : Any>(
    private val typeReference: TypeReference<T>,
    private val template: ReactiveRedisTemplate<String, T>,
    private val objectMapper: ObjectMapper,
) : CacheRepo<T> {
    @Suppress("UNCHECKED_CAST")
    override suspend fun remember(key: String, expiresIn: Duration, cacheFn: suspend () -> T): T {
        if (!template.hasKeyAndAwait(key)) {
            return cacheFn().also {
                template.opsForValue().setAndAwait(key, it, expiresIn.toJavaDuration())
            }
        }

        return objectMapper.convertValue(template.opsForValue().getAndAwait(key), typeReference)
    }

    override suspend fun query(key: String, expiresIn: Duration, cacheFn: suspend () -> T?): T? {
        if (!template.hasKeyAndAwait(key)) {
            return cacheFn()?.also {
                template.opsForValue().setAndAwait(key, it, expiresIn.toJavaDuration())
            }
        }

        return objectMapper.convertValue(template.opsForValue().getAndAwait(key), typeReference)
    }

    override suspend fun delete(key: String) {
        template.deleteAndAwait(key)
    }
}
