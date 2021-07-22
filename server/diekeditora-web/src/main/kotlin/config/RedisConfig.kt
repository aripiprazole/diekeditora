package com.diekeditora.web.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    val objectMapper: ObjectMapper,
    val connectionFactory: ReactiveRedisConnectionFactory,
) {

    @Bean
    fun <T> template(): ReactiveRedisTemplate<String, T> {
        val valueSerializer = Jackson2JsonRedisSerializer(Any::class.java).apply {
            setObjectMapper(objectMapper)
        } as RedisSerializer<T>

        return ReactiveRedisTemplate(
            connectionFactory,
            newSerializationContext<String, T>(StringRedisSerializer())
                .value(valueSerializer)
                .build()
        )
    }
}
