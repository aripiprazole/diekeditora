package com.diekeditora.app.config

import com.diekeditora.domain.page.AppCursor
import com.diekeditora.domain.page.AppEdge
import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.page.AppPageInfo
import com.diekeditora.infra.serializers.DateDeserializer
import com.diekeditora.infra.serializers.DateSerializer
import com.diekeditora.infra.serializers.InstantDeserializer
import com.diekeditora.infra.serializers.InstantSerializer
import com.diekeditora.infra.serializers.LocalDateDeserializer
import com.diekeditora.infra.serializers.LocalDateSerializer
import com.diekeditora.infra.serializers.LocalDateTimeDeserializer
import com.diekeditora.infra.serializers.LocalDateTimeSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.relay.Connection
import graphql.relay.ConnectionCursor
import graphql.relay.Edge
import graphql.relay.PageInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Configuration
class SerializerConfig {
    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()
        .registerModule(
            SimpleModule()
                .addSerializer(Instant::class.java, InstantSerializer())
                .addDeserializer(Instant::class.java, InstantDeserializer())
                .addSerializer(Date::class.java, DateSerializer())
                .addDeserializer(Date::class.java, DateDeserializer())
                .addSerializer(LocalDate::class.java, LocalDateSerializer())
                .addDeserializer(LocalDate::class.java, LocalDateDeserializer())
                .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())

                .addAbstractTypeMapping(Connection::class.java, AppPage::class.java)
                .addAbstractTypeMapping(Edge::class.java, AppEdge::class.java)
                .addAbstractTypeMapping(PageInfo::class.java, AppPageInfo::class.java)
                .addAbstractTypeMapping(ConnectionCursor::class.java, AppCursor::class.java)
        )
}
