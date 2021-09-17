package com.diekeditora.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class CacheProvider(
    @PublishedApi
    internal val objectMapper: ObjectMapper,
    private val connectionFactory: ReactiveRedisConnectionFactory,
) {
    @PublishedApi
    internal fun <T : Any> template(kClass: KClass<T>): ReactiveRedisTemplate<String, T> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = Jackson2JsonRedisSerializer(kClass.java).apply {
            setObjectMapper(objectMapper)
        }

        return ReactiveRedisTemplate(
            connectionFactory,
            newSerializationContext<String, T>()
                .key(keySerializer)
                .hashKey(keySerializer)
                .value(valueSerializer)
                .hashValue(valueSerializer)
                .build()
        )
    }

    final inline fun <reified T : Any> template(): ReactiveRedisTemplate<String, T> =
        template(T::class)

    @OptIn(ExperimentalStdlibApi::class)
    final inline fun <reified T : Any> repo(): CacheRepo<T> =
        CacheRepoImpl(jacksonTypeRef(), template(), objectMapper)
}
