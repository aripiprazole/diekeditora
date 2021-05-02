package com.lorenzoog.diekeditora.graphql.user

import com.expediagroup.graphql.server.operations.Query
import com.lorenzoog.diekeditora.dtos.EntityPage
import com.lorenzoog.diekeditora.dtos.toEntityPage
import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.services.UserService
import org.springframework.stereotype.Component

@Component
class UserQuery(val service: UserService) : Query {
    suspend fun user(username: String): User? {
        return service.findByUsername(username)
    }

    suspend fun users(page: Int): EntityPage {
        return service.findPaginated(page).toEntityPage()
    }
}
