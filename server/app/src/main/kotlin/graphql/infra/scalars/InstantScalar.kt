package com.diekeditora.infra.graphql.scalars

import com.diekeditora.domain.graphql.Scalar
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.utils.jacksonScalar
import org.springframework.stereotype.Component
import java.time.Instant

@Component
internal class InstantScalar(objectMapper: ObjectMapper) :
    Scalar<Instant, Long> by jacksonScalar(objectMapper)
        .deserialize(String::toLong)
