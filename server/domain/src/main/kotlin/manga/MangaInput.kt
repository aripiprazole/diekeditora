package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
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
    init {
        validate(this) {
            validate(MangaInput::title).hasSize(min = 4, max = 32).isNotBlank()
            validate(MangaInput::description).hasSize(max = 1000).isNotBlank()
            validate(MangaInput::advisory).isIn(10, 12, 14, 16, 18)
        }
    }

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
