package com.diekeditora.graphql.infra.scalars

import com.diekeditora.graphql.domain.Scalar
import com.diekeditora.id.domain.UniqueId
import graphql.language.StringValue
import graphql.schema.Coercing
import org.springframework.stereotype.Component

@Component
class UniqueIdScalar : Scalar<UniqueId, String> {
    override val klass = UniqueId::class
    override val coercing = object : Coercing<UniqueId, String> {
        override fun serialize(dataFetcherResult: Any?): String {
            return dataFetcherResult.toString()
        }

        override fun parseValue(input: Any?): UniqueId {
            return UniqueId(input.toString())
        }

        override fun parseLiteral(input: Any?): UniqueId {
            if (input !is StringValue) error("Could not parse unique id from non-string value")

            return UniqueId(input.value)
        }
    }
}
