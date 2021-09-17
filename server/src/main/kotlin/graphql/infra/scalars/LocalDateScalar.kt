package com.diekeditora.graphql.infra.scalars

import com.diekeditora.graphql.domain.Scalar
import com.diekeditora.graphql.infra.utils.jacksonScalar
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
internal class LocalDateScalar(objectMapper: ObjectMapper) :
    Scalar<LocalDate, String> by jacksonScalar(objectMapper)
        .deserializeString()
