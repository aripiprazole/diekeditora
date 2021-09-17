package com.diekeditora.shared.domain

import com.diekeditora.id.domain.RefId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLIgnore
interface MutableEntity<E, ID : RefId<*>> : Entity<ID> {
    @GraphQLIgnore
    fun update(with: E): E
}
