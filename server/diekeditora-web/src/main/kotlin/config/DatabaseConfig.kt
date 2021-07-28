package com.diekeditora.web.config

import com.diekeditora.domain.id.UniqueId
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import java.util.UUID

@Configuration
@OptIn(ExperimentalStdlibApi::class)
class DatabaseConfig(
    @Qualifier("actualConnectionFactory")
    val connectionFactory: ConnectionFactory,
    val r2dbcProperties: R2dbcProperties,
) : AbstractR2dbcConfiguration() {
    override fun getCustomConverters(): List<Any> = buildList {
        add(Converter<UniqueId, UUID> { id -> UUID.fromString(id.value) })
        add(Converter<UUID, UniqueId> { value -> UniqueId(value.toString()) })
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().apply {
            val populator = CompositeDatabasePopulator().apply {
                val migrations = ClassPathResource("db/migration")

                migrations.file.list().orEmpty().sorted().forEach {
                    val resource = ClassPathResource("db/migration/$it")

                    addPopulators(ResourceDatabasePopulator(resource))
                }
            }

            setConnectionFactory(connectionFactory)
            setDatabasePopulator(populator)
        }

    @Bean
    @Primary
    override fun connectionFactory(): ConnectionPool {
        return ConnectionPool(
            ConnectionPoolConfiguration
                .builder(connectionFactory)
                .initialSize(r2dbcProperties.pool.initialSize)
                .maxAcquireTime(r2dbcProperties.pool.maxAcquireTime)
                .maxCreateConnectionTime(r2dbcProperties.pool.maxCreateConnectionTime)
                .maxIdleTime(r2dbcProperties.pool.maxIdleTime)
                .maxLifeTime(r2dbcProperties.pool.maxLifeTime)
                .maxSize(r2dbcProperties.pool.maxSize)
                .validationQuery(r2dbcProperties.pool.validationQuery)
                .validationDepth(r2dbcProperties.pool.validationDepth)
                .build()
        )
    }
}
