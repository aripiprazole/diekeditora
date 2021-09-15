package com.diekeditora.infra.graphql.scalars

import com.diekeditora.domain.graphql.Scalar
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.utils.jacksonScalar
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
internal class LocalDateScalar(objectMapper: ObjectMapper) :
    Scalar<LocalDate, String> by jacksonScalar(objectMapper)
        .deserializeString()
