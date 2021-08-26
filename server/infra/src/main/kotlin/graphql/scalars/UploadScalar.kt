package com.diekeditora.infra.graphql.scalars

import com.diekeditora.domain.file.Upload
import com.diekeditora.domain.graphql.Scalar
import graphql.schema.Coercing
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component

@Component
internal class UploadScalar : Scalar<Upload, Unit> {
    override val klass = Upload::class
    override val coercing = object : Coercing<Upload, Unit> {
        override fun serialize(dataFetcherResult: Any?) {
            error("Could not parse literal Upload")
        }

        override fun parseValue(input: Any?): Upload {
            val part = input as? FilePart
                ?: error("Could not parse a non-file part object in Upload scalar")

            return Upload(part)
        }

        override fun parseLiteral(input: Any?): Upload {
            error("Could not parse literal Upload")
        }
    }
}
