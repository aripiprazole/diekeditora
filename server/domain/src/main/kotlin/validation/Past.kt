package com.diekeditora.domain.validation

import com.expediagroup.graphql.generator.annotations.GraphQLDirective
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.introspection.Introspection.DirectiveLocation.ARGUMENT_DEFINITION
import graphql.introspection.Introspection.DirectiveLocation.FIELD_DEFINITION
import graphql.introspection.Introspection.DirectiveLocation.INPUT_FIELD_DEFINITION

@GraphQLDirective(locations = [FIELD_DEFINITION, INPUT_FIELD_DEFINITION, ARGUMENT_DEFINITION])
annotation class Past(@GraphQLIgnore val message: String = "The date input must be in the past")
