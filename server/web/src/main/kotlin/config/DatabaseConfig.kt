package com.diekeditora.web.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
@OptIn(ExperimentalStdlibApi::class)
class DatabaseConfig(val connectionFactory: ConnectionFactory) : AbstractR2dbcConfiguration() {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().apply {
            val populator = CompositeDatabasePopulator().apply {
                val migrations = ClassPathResource("db/migration")

                migrations.file.list().orEmpty().forEach {
                    val resource = ClassPathResource("db/migration/$it")

                    addPopulators(ResourceDatabasePopulator(resource))
                }
            }

            setConnectionFactory(connectionFactory)
            setDatabasePopulator(populator)
        }

    override fun connectionFactory(): ConnectionFactory = connectionFactory
}
