package com.diekeditora.domain

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLIgnore
interface Entity<T> where T : Entity<T>, T : Any {
    @GraphQLIgnore
    fun update(with: T): T
}
