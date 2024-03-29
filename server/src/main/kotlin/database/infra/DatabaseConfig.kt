package com.diekeditora.database.infra

import com.diekeditora.database.domain.AuthorityId
import com.diekeditora.database.domain.ChapterId
import com.diekeditora.database.domain.GenreId
import com.diekeditora.database.domain.InvoiceId
import com.diekeditora.database.domain.MangaId
import com.diekeditora.database.domain.NewsletterId
import com.diekeditora.database.domain.NotificationId
import com.diekeditora.database.domain.ProfileId
import com.diekeditora.database.domain.RoleId
import com.diekeditora.database.domain.UserId
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.shared.infra.RefIdConverter
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
    val actualConnectionFactory: ConnectionFactory,
    val r2dbcProperties: R2dbcProperties,
) : AbstractR2dbcConfiguration() {
    override fun getCustomConverters(): List<Any> = buildList {
        add(Converter<UniqueId, UUID> { id -> runCatching { UUID.fromString(id.rawId) }.getOrNull() })
        add(Converter<UUID, UniqueId> { value -> UniqueId(value.toString()) })

        add(RefIdConverter(AuthorityId.New::class))
        add(Converter<UUID, AuthorityId> { AuthorityId.Persisted(it) })

        add(RefIdConverter(ChapterId.New::class))
        add(Converter<UUID, ChapterId> { ChapterId.Persisted(it) })

        add(RefIdConverter(GenreId.New::class))
        add(Converter<UUID, GenreId> { GenreId.Persisted(it) })

        add(RefIdConverter(InvoiceId.New::class))
        add(Converter<UUID, InvoiceId> { InvoiceId.Persisted(it) })

        add(RefIdConverter(MangaId.New::class))
        add(Converter<UUID, MangaId> { MangaId.Persisted(it) })

        add(RefIdConverter(NewsletterId.New::class))
        add(Converter<UUID, NewsletterId> { NewsletterId.Persisted(it) })

        add(RefIdConverter(NotificationId.New::class))
        add(Converter<UUID, NotificationId> { NotificationId.Persisted(it) })

        add(RefIdConverter(ProfileId.New::class))
        add(Converter<UUID, ProfileId> { ProfileId.Persisted(it) })

        add(RefIdConverter(RoleId.New::class))
        add(Converter<UUID, RoleId> { RoleId.Persisted(it) })

        add(RefIdConverter(UserId.New::class))
        add(Converter<UUID, UserId> { UserId.Persisted(it) })
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
                .builder(actualConnectionFactory)
                .initialSize(r2dbcProperties.pool.initialSize)
                .apply { r2dbcProperties.pool.maxAcquireTime?.let(::maxAcquireTime) }
                .apply { r2dbcProperties.pool.maxCreateConnectionTime?.let(::maxCreateConnectionTime) }
                .apply { r2dbcProperties.pool.maxIdleTime?.let(::maxIdleTime) }
                .apply { r2dbcProperties.pool.maxLifeTime?.let(::maxLifeTime) }
                .apply { r2dbcProperties.pool.validationDepth?.let(::validationDepth) }
                .apply { r2dbcProperties.pool.validationQuery?.let(::validationQuery) }
                .maxSize(r2dbcProperties.pool.maxSize)
                .build()
        )
    }
}
