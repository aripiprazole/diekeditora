package com.diekeditora.app.resources.manga

import com.diekeditora.domain.graphql.Authenticated
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.diekeditora.domain.page.AppPage
import com.diekeditora.infra.graphql.AuthGraphQLContext
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
        return mangaService
            .findMangas(first, after, orderBy ?: MangaSort.Empty, filterBy.orEmpty().toSet())
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Returns similar manga recommendations page")
    suspend fun similarMangas(
        ctx: AuthGraphQLContext,
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("Manga title") after: UniqueId? = null,
        @GraphQLDescription("Manga search order") orderBy: MangaSort? = null,
        @GraphQLDescription("Manga search filter categories") filterBy: List<String>? = null,
    ): Connection<Manga> {
        val latestRead = mangaService.findLastReadMangas(ctx.user, 1)
        val manga = latestRead.edges.map { it.node }.firstOrNull() ?: return AppPage.empty()

        return mangaService.findSimilarMangas(
            manga, first, after, orderBy ?: MangaSort.Empty, filterBy.orEmpty().toSet()
        )
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Returns manga recommendations page")
    suspend fun recommendedMangas(
        ctx: AuthGraphQLContext,
        @GraphQLDescription("Node list size") first: Int,
        @GraphQLDescription("Manga title") after: UniqueId? = null,
        @GraphQLDescription("Manga search order") orderBy: MangaSort? = null,
        @GraphQLDescription("Manga search filter categories") filterBy: List<String>? = null,
    ): Connection<Manga> {
        return mangaService.findRecommendedMangas(
            ctx.user, first, after, orderBy ?: MangaSort.Empty, filterBy.orEmpty().toSet()
        )
    }
}
