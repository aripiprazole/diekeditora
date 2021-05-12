package com.lorenzoog.diekeditora.web.graphql.user

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class UserQuery : Query {
    suspend fun user(username: String): String {
        TODO()
    }
}
