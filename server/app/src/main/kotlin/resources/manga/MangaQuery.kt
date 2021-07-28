package com.diekeditora.app.resources.manga

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.stereotype.Component

@Component
class MangaQuery(val mangaService: MangaService) : Query {

    @GraphQLDescription("Finds manga connection")
    suspend fun mangas(
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("Manga title") after: String,
        @GraphQLDescription("Manga search order") orderBy: MangaSort = MangaSort.empty(),
        @GraphQLDescription("Manga search filter categories") filterBy: List<String> = emptyList(),
    ): Connection<Manga> {
        return mangaService.findMangas(first, after, orderBy, filterBy.toSet())
    }

    @GraphQLDescription("Finds manga by its title")
    suspend fun manga(title: String): Manga? {
        return mangaService.findMangaByTitle(title)
    }
}
