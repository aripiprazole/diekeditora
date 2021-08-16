package com.diekeditora.domain.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDirective
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLDirective
annotation class Secured(@GraphQLIgnore vararg val authorities: String)
