package com.diekeditora.manga.resources

import com.diekeditora.graphql.infra.AuthGraphQLContext
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import com.diekeditora.manga.domain.MangaService
import com.diekeditora.manga.domain.MangaSort
import com.diekeditora.page.infra.AppPage
import com.diekeditora.security.domain.Authenticated
import com.diekeditora.security.domain.Secured
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
