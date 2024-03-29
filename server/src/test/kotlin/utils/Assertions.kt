package com.diekeditora.tests.utils

import com.diekeditora.tests.graphql.NOT_ENOUGH_AUTHORITIES
import com.expediagroup.graphql.generator.exceptions.GraphQLKotlinException
import org.junit.jupiter.api.assertThrows

inline fun assertGraphQLForbidden(crossinline block: () -> Unit) {
    assertThrows<GraphQLKotlinException>(NOT_ENOUGH_AUTHORITIES) {
        block()
    }
}
