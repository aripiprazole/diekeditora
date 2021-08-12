package com.diekeditora.domain.manga

import com.diekeditora.domain.graphql.DivisibleBy
import com.diekeditora.domain.graphql.Max
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank
import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations

@GraphQLValidObjectLocations([Locations.INPUT_OBJECT])
@Suppress("Detekt.MagicNumber")
data class MangaInput(
    @NotBlank @Max(32) @Min(4) val title: String,
    @NotBlank @Max(1000) val description: String,
    @DivisibleBy(2) @Max(18) @Min(10) val advisory: Int? = null,
    val competing: Boolean,
) {
    fun toManga(uid: UniqueId): Manga {
        return Manga(
            uid = uid,
            title = title,
            description = description,
            competing = competing,
            advisory = advisory,
        )
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
