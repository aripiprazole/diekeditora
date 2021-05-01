package com.lorenzoog.diekeditora.graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder

interface GraphQLExtension {
    fun SchemaBuilder.setup()
}
