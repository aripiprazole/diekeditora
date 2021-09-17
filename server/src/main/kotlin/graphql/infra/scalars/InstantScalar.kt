package com.diekeditora.graphql.infra.scalars

import com.diekeditora.graphql.domain.Scalar
import com.diekeditora.graphql.infra.utils.jacksonScalar
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.Instant

@Component
internal class InstantScalar(objectMapper: ObjectMapper) :
    Scalar<Instant, Long> by jacksonScalar(objectMapper)
        .deserialize(String::toLong)
