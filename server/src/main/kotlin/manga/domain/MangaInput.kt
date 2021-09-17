package com.diekeditora.manga.domain

import com.diekeditora.id.domain.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import org.valiktor.functions.hasSize
import org.valiktor.functions.isIn
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

@GraphQLValidObjectLocations([Locations.INPUT_OBJECT])
@Suppress("Detekt.MagicNumber")
data class MangaInput(
    val title: String,
    val description: String,
    val advisory: Int? = null,
    val competing: Boolean,
) {
    fun toManga(uid: UniqueId): Manga = validate(
        Manga(
            uid = uid, title = title, description = description,
            competing = competing, advisory = advisory,
        )
    ) {
        validate(Manga::title).hasSize(min = 4, max = 32).isNotBlank()
        validate(Manga::description).hasSize(max = 1000).isNotBlank()
        validate(Manga::advisory).isIn(10, 12, 14, 16, 18)
    }

    companion object {
        fun from(manga: Manga): MangaInput {
            return MangaInput(
                title = manga.title,
                competing = manga.competing,
                description = manga.description,
                advisory = manga.advisory,
            )
        }
    }
}
