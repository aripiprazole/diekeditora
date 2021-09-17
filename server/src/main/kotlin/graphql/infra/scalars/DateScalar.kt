package com.diekeditora.infra.graphql.scalars

import com.diekeditora.domain.graphql.Scalar
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.utils.jacksonScalar
import org.springframework.stereotype.Component
import java.util.Date

@Component
internal class DateScalar(objectMapper: ObjectMapper) :
    Scalar<Date, Long> by jacksonScalar(objectMapper)
        .deserialize(String::toLong)
