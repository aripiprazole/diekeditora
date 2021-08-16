package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.validation.DivisibleBy
import com.diekeditora.domain.validation.Max
import com.diekeditora.domain.validation.Min
import com.diekeditora.domain.validation.NotBlank
import com.diekeditora.domain.validation.Size
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations

@GraphQLValidObjectLocations([Locations.INPUT_OBJECT])
@Suppress("Detekt.MagicNumber")
data class MangaInput(
    @NotBlank @Size(min = 8, max = 32) val title: String,
    @NotBlank @Size(max = 2000) val description: String,
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
