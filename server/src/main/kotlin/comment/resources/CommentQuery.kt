package com.diekeditora.comment.resources

import com.diekeditora.comment.domain.Comment
import com.diekeditora.comment.domain.CommentService
import com.diekeditora.comment.domain.ReportService
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.security.domain.Secured
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class CommentQuery(val reportService: ReportService, val commentService: CommentService) : Query {
    @Secured
    @PreAuthorize("hasAuthority('comment.view')")
    @GraphQLDescription("Returns reported comment page")
    suspend fun reportedComments(first: Int, after: UniqueId? = null): Connection<Comment> {
        return reportService.findReportedComments(first, after)
    }

    @Secured
    @PreAuthorize("hasAuthority('comment.view')")
    @GraphQLDescription("Returns comment detail by its uid")
    suspend fun comment(uid: UniqueId): Comment? {
        return commentService.findCommentByUid(uid)
    }
}
