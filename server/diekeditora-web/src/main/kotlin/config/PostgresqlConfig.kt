package com.diekeditora.web.config

import com.diekeditora.domain.profile.Gender
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.Option
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PostgresqlConfig(val props: R2dbcProperties) {
    @Bean
    @Qualifier("actualConnectionFactory")
    @ConditionalOnMissingBean
    fun connectionFactory(): PostgresqlConnectionFactory {
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

    private fun R2dbcProperties.toOptions(): ConnectionFactoryOptions {
        return ConnectionFactoryOptions.parse(url)
            .mutate()
            .option(ConnectionFactoryOptions.USER, username)
            .option(ConnectionFactoryOptions.PASSWORD, password)
            .option(ConnectionFactoryOptions.DATABASE, name)
            .also { builder ->
                properties.forEach { (name, value) ->
                    builder.option(Option.valueOf(name), value)
                }
            }
            .build()
    }
}
