package com.lorenzoog.diekeditora.resources

import com.apurebase.kgraphql.schema.Schema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/graphql")
class GraphQLResource(val schema: Schema) {
    @GetMapping(produces = ["application/json"])
    suspend fun graphql(@RequestParam query: String): String {
        return schema.execute(query)
    }
}
