package com.lorenzoog.diekeditora.config

import com.apurebase.kgraphql.KGraphQL
import com.apurebase.kgraphql.schema.Schema
import com.lorenzoog.diekeditora.graphql.UserExtension
import com.lorenzoog.diekeditora.utils.serializable
import com.lorenzoog.diekeditora.utils.with
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Configuration
class GraphQLConfig(val json: Json, val userSchema: UserExtension) {
    @Bean
    fun schema(): Schema = KGraphQL.schema {
        with(userSchema)

        serializable<UUID>(json)
        serializable<LocalDate>(json)
        serializable<LocalDateTime>(json)
        serializable<Instant>(json)
    }
}
