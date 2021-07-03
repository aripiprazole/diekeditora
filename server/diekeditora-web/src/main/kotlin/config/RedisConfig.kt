package com.diekeditora.web.config

import com.diekeditora.domain.notification.Notification
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    val objectMapper: ObjectMapper,
    val connectionFactory: ReactiveRedisConnectionFactory,
) {
    @Bean
    fun notificationRedisTemplate() = createTemplate<String, Notification>()

    private inline fun <K, reified T : Any> createTemplate(): ReactiveRedisTemplate<K, T> {
        val valueSerializer = Jackson2JsonRedisSerializer(T::class.java).apply {
            setObjectMapper(objectMapper)
        }

        return ReactiveRedisTemplate(
            connectionFactory,
            newSerializationContext<K, T>(StringRedisSerializer())
                .value(valueSerializer)
                .build()
        )
    }
}
