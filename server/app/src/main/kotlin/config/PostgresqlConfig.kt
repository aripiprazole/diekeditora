package com.diekeditora.app.config

import com.diekeditora.domain.profile.Gender
import com.diekeditora.app.utils.toOptions
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PostgresqlConfig(val props: R2dbcProperties) {
    @Bean("actualConnectionFactory")
    @ConditionalOnMissingBean(name = ["actualConnectionFactory"])
    fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionFactoryProvider
                .builder(props.toOptions())
                .codecRegistrar(
                    EnumCodec.builder()
                        .withEnum("gender", Gender::class.java)
                        .build()
                )
                .build()
        )
    }
}
