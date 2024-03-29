package com.diekeditora.database.infra

import com.diekeditora.profile.domain.Gender
import com.diekeditora.shared.infra.toOptions
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
