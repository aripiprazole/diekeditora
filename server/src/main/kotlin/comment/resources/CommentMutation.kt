package com.diekeditora.comment.resources

import com.diekeditora.comment.domain.Comment
import com.diekeditora.comment.domain.CommentInput
import com.diekeditora.comment.domain.CommentService
import com.diekeditora.comment.domain.ReportReason
import com.diekeditora.comment.domain.ReportService
import com.diekeditora.graphql.infra.AuthGraphQLContext
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.id.domain.UniqueIdService
import com.diekeditora.manga.domain.MangaService
import com.diekeditora.security.domain.Authenticated
import com.diekeditora.security.domain.Secured
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
