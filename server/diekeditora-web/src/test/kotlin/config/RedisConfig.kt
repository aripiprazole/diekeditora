package com.diekeditora.web.tests.config

import com.diekeditora.web.tests.props.RedisProps
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class RedisConfig(props: RedisProps) {
    val redisServer = RedisServer(props.redisPort)

    @PostConstruct
    fun postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }
}
