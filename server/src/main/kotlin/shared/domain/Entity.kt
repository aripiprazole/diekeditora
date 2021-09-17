package com.diekeditora.shared.domain

import com.diekeditora.id.domain.RefId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.shared.infra.findPropertyByAnnotation
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore

@GraphQLIgnore
interface Entity<ID : RefId<*>> {
    val id: ID @GraphQLIgnore get

    val cursor: String
        @GraphQLIgnore
        @JsonIgnore
        get() = this::class.findPropertyByAnnotation<Cursor>()?.getter
            ?.call(this)
            ?.toString()
            ?: error("Can not find cursor in entity ${this::class.simpleName}")
}
