package com.diekeditora.app.tests.props

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RedisProps(
    @Value("\${spring.redis.port}") val redisPort: Int,
    @Value("\${spring.redis.host}") val redisHost: String,
)
