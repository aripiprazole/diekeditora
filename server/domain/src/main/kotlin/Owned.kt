package com.diekeditora.domain

import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLIgnore
interface Owned {
    val ownerId: UniqueId

    @GraphQLIgnore
    fun <T> belongsTo(entity: Entity<T>): Boolean where T : Any, T : Entity<T> {
        return entity.id == ownerId
    }
}
