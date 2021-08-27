package com.diekeditora.app.resources.comment

import com.diekeditora.domain.comment.Comment
import com.diekeditora.domain.comment.CommentInput
import com.diekeditora.domain.comment.CommentService
import com.diekeditora.domain.comment.ReportReason
import com.diekeditora.domain.comment.ReportService
import com.diekeditora.domain.graphql.Authenticated
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.id.UniqueIdService
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.infra.graphql.AuthGraphQLContext
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

@Component
class CommentMutation(
    val mangaService: MangaService,
    val commentService: CommentService,
    val reportService: ReportService,
    val uidService: UniqueIdService,
) : Mutation {
    @Secured
    @Authenticated
    @GraphQLDescription("Creates a new comment with provided data")
    suspend fun createComment(ctx: AuthGraphQLContext, input: CommentInput): Comment? {
        val manga = mangaService.findMangaByUid(input.mangaUid) ?: return null

        val uid = uidService.generateUniqueId()
        val mangaId = manga.id ?: error("Manga id must be not null")
        val userId = ctx.user.id ?: error("User id must be not null")

        return commentService.createComment(manga, input.toComment(uid, mangaId, userId))
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Report a comment with `reason` by its uid")
    suspend fun reportComment(
        ctx: AuthGraphQLContext,
        uid: UniqueId,
        reason: ReportReason,
    ): Comment? {
        val comment = commentService.findCommentByUid(uid) ?: return null

        ctx {
            hasAuthority("comment.destroy")
            user.own(comment)
        }

        return reportService.reportComment(comment, reason)
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Deletes a comment by its uid")
    suspend fun deleteComment(ctx: AuthGraphQLContext, uid: UniqueId): Comment? {
        val comment = commentService.findCommentByUid(uid) ?: return null

        ctx {
            hasAuthority("comment.destroy")
            user.own(comment)
        }

        return commentService.deleteComment(comment)
    }
}
