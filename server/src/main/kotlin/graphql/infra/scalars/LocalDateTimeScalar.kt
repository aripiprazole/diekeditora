package com.diekeditora.graphql.infra.scalars

import com.diekeditora.graphql.domain.Scalar
import com.diekeditora.graphql.infra.utils.jacksonScalar
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class LocalDateTimeScalar(objectMapper: ObjectMapper) :
    Scalar<LocalDateTime, String> by jacksonScalar(objectMapper)
        .deserializeString()
