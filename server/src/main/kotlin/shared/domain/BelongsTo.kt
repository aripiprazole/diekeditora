package com.diekeditora.shared.domain

import com.diekeditora.id.domain.RefId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLIgnore
interface BelongsTo<OwnerID : RefId<*>> {
    val ownerId: OwnerID

    @GraphQLIgnore
    fun belongsTo(entity: Entity<OwnerID>): Boolean {
        return ownerId == entity.id
    }
}
