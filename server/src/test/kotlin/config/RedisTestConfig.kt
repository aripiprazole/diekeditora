package com.diekeditora.app.tests.config

import com.diekeditora.app.tests.props.RedisProps
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class RedisTestConfig(props: RedisProps) {
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
