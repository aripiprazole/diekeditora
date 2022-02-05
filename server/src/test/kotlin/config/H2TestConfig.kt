package com.diekeditora.tests.config

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class H2TestConfig {
    @Bean("actualConnectionFactory")
    fun connectionFactory(): H2ConnectionFactory {
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory("r2dbc:h2:mem:///database;DATABASE_TO_UPPER=false;MODE=POSTGRESQL")
                .build()
        )
    }
}
