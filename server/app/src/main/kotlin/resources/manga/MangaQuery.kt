package com.diekeditora.app.resources.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.stereotype.Component

@Component
class MangaQuery(val mangaService: MangaService) : Query {
    @GraphQLDescription("Returns manga details by its uid")
    suspend fun manga(uid: UniqueId): Manga? {
        return mangaService.findMangaByUid(uid)
    }

    @GraphQLDescription("Returns manga page")
    suspend fun mangas(
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("Manga title") after: UniqueId? = null,
        @GraphQLDescription("Manga search order") orderBy: MangaSort? = null,
        @GraphQLDescription("Manga search filter categories") filterBy: List<String>? = null,
    ): Connection<Manga> {
        return mangaService.findMangas(
            first,
            after,
            orderBy ?: MangaSort.Empty,
            filterBy.orEmpty().toSet()
        )
    }
}
