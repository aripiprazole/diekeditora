package com.diekeditora.web.tests.utils

import com.diekeditora.web.tests.graphql.GraphQLException
import com.diekeditora.web.tests.graphql.NOT_ENOUGH_AUTHORITIES
import org.junit.jupiter.api.assertThrows

inline fun assertGraphQLForbidden(crossinline block: () -> Unit) {
    assertThrows<GraphQLException>(NOT_ENOUGH_AUTHORITIES) {
        block()
    }
}
