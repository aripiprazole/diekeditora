package com.diekeditora.domain.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDirective
import graphql.introspection.Introspection.DirectiveLocation.FIELD_DEFINITION
import org.intellij.lang.annotations.Language

@GraphQLDirective(locations = [FIELD_DEFINITION])
annotation class Regex(@Language("RegExp") val regex: String)
