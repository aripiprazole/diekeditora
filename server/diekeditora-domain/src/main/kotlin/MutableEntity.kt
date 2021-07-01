package com.diekeditora.domain

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLIgnore
interface MutableEntity<T> where T : MutableEntity<T>, T : Any {
    @GraphQLIgnore
    fun update(with: T): T
}
