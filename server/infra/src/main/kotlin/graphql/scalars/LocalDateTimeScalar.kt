package com.diekeditora.infra.graphql.scalars

import com.diekeditora.domain.graphql.Scalar
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.utils.jacksonScalar
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class LocalDateTimeScalar(objectMapper: ObjectMapper) :
    Scalar<LocalDateTime, String> by jacksonScalar(objectMapper)
        .deserializeString()
