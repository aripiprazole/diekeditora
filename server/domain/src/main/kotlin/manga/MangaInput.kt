package com.diekeditora.domain.manga

import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations

@GraphQLValidObjectLocations([Locations.INPUT_OBJECT])
data class MangaInput(
    val title: String,
    val competing: Boolean,
    val summary: String,
    val advisory: Int = 0,
)
