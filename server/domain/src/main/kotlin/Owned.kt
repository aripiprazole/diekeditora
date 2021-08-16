package com.diekeditora.domain

import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLIgnore
interface Owned<T> where T : Any, T : Entity<T> {
    val ownerId: UniqueId
}
