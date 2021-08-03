package com.diekeditora.domain

import com.diekeditora.domain.page.Cursor
import com.diekeditora.shared.findPropertyByAnnotation
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore

@GraphQLIgnore
interface Entity<T> where T : Entity<T>, T : Any {
    @GraphQLIgnore
    @get:JsonIgnore
    val cursor: String
        get() = this::class.findPropertyByAnnotation<Cursor>()?.getter
            ?.call(this)
            ?.toString()
            ?: error("Can not find cursor in entity ${this::class.simpleName}")
}
