package com.diekeditora.shared.infra

import com.diekeditora.page.infra.AppCursor
import com.diekeditora.page.infra.AppEdge
import com.diekeditora.page.infra.AppPage
import com.diekeditora.page.infra.AppPageInfo
import com.diekeditora.serializers.DateDeserializer
import com.diekeditora.serializers.DateSerializer
import com.diekeditora.serializers.InstantDeserializer
import com.diekeditora.serializers.InstantSerializer
import com.diekeditora.serializers.LocalDateDeserializer
import com.diekeditora.serializers.LocalDateSerializer
import com.diekeditora.serializers.LocalDateTimeDeserializer
import com.diekeditora.serializers.LocalDateTimeSerializer
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
