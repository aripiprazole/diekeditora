package com.diekeditora.domain

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.shared.findPropertyByAnnotation
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore

@GraphQLIgnore
interface Entity<T> where T : Entity<T>, T : Any {
    @get:GraphQLIgnore
    val id: UniqueId?

    val cursor: String
        @GraphQLIgnore
        @JsonIgnore
        get() = this::class.findPropertyByAnnotation<Cursor>()?.getter
            ?.call(this)
            ?.toString()
            ?: error("Can not find cursor in entity ${this::class.simpleName}")
}
