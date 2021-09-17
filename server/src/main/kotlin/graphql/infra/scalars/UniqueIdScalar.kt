package com.diekeditora.infra.graphql.scalars

import com.diekeditora.domain.graphql.Scalar
import com.diekeditora.domain.id.UniqueId
import graphql.language.StringValue
import graphql.schema.Coercing
import org.springframework.stereotype.Component

@Component
internal class UniqueIdScalar : Scalar<UniqueId, String> {
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
