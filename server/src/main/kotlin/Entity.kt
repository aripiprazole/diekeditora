package com.diekeditora

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.shared.infra.findPropertyByAnnotation
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
