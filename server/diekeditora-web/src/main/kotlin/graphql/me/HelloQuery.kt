package com.diekeditora.web.graphql.me

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class HelloQuery : Query {
    fun hello(): String {
        return "Hello, world"
    }
}
