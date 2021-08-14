package com.diekeditora.domain.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDirective
import graphql.introspection.Introspection.DirectiveLocation.FIELD_DEFINITION

@GraphQLDirective(locations = [FIELD_DEFINITION])
annotation class Min(val value: Int, val message: String = "")